package com.yz.zwzb.controllers;

import com.yz.zwzb.domain.*;
import com.yz.zwzb.domain.enums.RoomStatusEnum;
import com.yz.zwzb.domain.response.Resp;
import com.yz.zwzb.service.MatchService;
import com.yz.zwzb.service.RoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Controller
public class AppController
{
    @Resource
    private SimpMessagingTemplate template;

    // 用户创建房间
    @MessageMapping("/createRoom")
    public void createRoom(Principal principal)
    {
        String name = principal.getName();
        Long roomId = RoomService.createRoom();
        Resp resp = new Resp("createRoom");
        resp.fill("roomId", roomId);
        template.convertAndSendToUser(name, "/queue/reply", resp);

    }

    // 用户邀请其他用户
    @MessageMapping("/apply")
    public void apply(Principal principal, HashMap<String, String> req)
    {
        Resp message = new Resp("apply");
        message.fill("roomId", req.get("roomId")).fill("playerId", principal.getName());
        template.convertAndSend("/topic", message);
    }


    // 用户回答问题
    @MessageMapping("/answer")
    public void answer(Principal principal, HashMap<String, Object> req)
    {
        String matchId = req.get("matchId").toString();
        String roomId = req.get("roomId").toString();
        Integer answer = (Integer)req.get("answer");
        Long matchIdLong = Long.parseLong(matchId);
        Long roomIdLong = Long.parseLong(roomId);

        Match match = MatchService.getMatch(matchIdLong);
        List<Step> steps = match.getSteps();
        int currStep = match.getCurrStep();
        if (currStep < steps.size())
        {
            Step step = steps.get(currStep);
            HashMap<String, Result> playerAnswer = step.getPlayerAnswer();
            playerAnswer.put(principal.getName(), new Result(answer).judg(step.getAnswer()));
            int size = playerAnswer.size();
            if(size == match.getPlayerAccounts().size()){
                // 环节结束，通知这道题目的所有人答题结果，并下发下一道题目，直到环节结束
                sendStep2end(match);
                currStep = match.getCurrStep();
                if (currStep >= match.getSteps().size())
                {
                    // 比赛结束，发送比赛结果
                    MatchService.countResult(match);
                    Room room = RoomService.getRoom(roomIdLong);
                    room.setStatus(RoomStatusEnum.finished);
                    List<String> players = match.getPlayerAccounts();
                    HashMap<String, PlayerResultOneMatch> matchResult = match.getMatchResult();
                    for (String player : players)
                    {
                        template.convertAndSendToUser(player, "/queue/reply",//
                                new Resp("matchEnd").fill("matchResult", matchResult));
                    }
                    template.convertAndSend("/topic", new Resp("msg").fill("text", match.getResult()));
                }
            }
        }
    }

    // 用户加入房间
    @MessageMapping("/joinRoom/{roomId}")
    public void joinRoom(Principal principal, @DestinationVariable Long roomId)
    {
        String playerAccount = principal.getName();
        Room room = RoomService.joinRoom(roomId, playerAccount);
        updateRoomPlayerList(room);
        if (RoomStatusEnum.fighting.equals(room.getStatus()))
        {
            sendStepBegin(room.getMatchId());
        }
    }

    // 用户退出房间
    @MessageMapping("/exitRoom/{roomId}")
    public void exitRoom(Principal principal, @DestinationVariable Long roomId)
    {
        String playerAccount = principal.getName();
        Room room = RoomService.exitRoom(roomId, playerAccount);
        template.convertAndSendToUser(principal.getName(), "/queue/reply", new Resp("updateRoom").fill("room", room));
        updateRoomPlayerList(RoomService.getRoom(roomId));
    }

    private void updateRoomPlayerList(Room room)
    {
        if (room == null)
        {
            return;
        }
        // 通知房间的其他人，有新的用户状态
        HashSet<String> playerIds = room.getPlayerAccounts();
        Iterator<String> iterator = playerIds.iterator();
        while (iterator.hasNext())
        {
            String next = iterator.next();
            template.convertAndSendToUser(next, "/queue/reply",//
                    new Resp("updateRoom").fill("room", room));
        }
    }

    private void sendStepBegin(Long matchId)
    {
        Match match = MatchService.getMatch(matchId);
        if (match != null)
        {
            List<String> players = match.getPlayerAccounts();
            List<Step> steps = match.getSteps();
            int currStep = match.getCurrStep();
            Step step = steps.get(currStep);
            for (String player : players)
            {
                template.convertAndSendToUser(player, "/queue/reply",//
                        new Resp("stepBegin").fill("step", step));
            }
        }
    }
    private void sendStep2end(Match match)
    {
        if (match != null)
        {
            List<Step> steps = match.getSteps();
            int currStep = match.getCurrStep();
            Step step = steps.get(currStep);

            int nextStepIndex = currStep + 1;
            int maxStepIndex = steps.size() - 1;
            Step nextStep = null;
            if(nextStepIndex <= maxStepIndex)
            {
                nextStep = steps.get(nextStepIndex);
            }

            match.setCurrStep(nextStepIndex);

            List<String> players = match.getPlayerAccounts();
            for (String player : players)
            {
                template.convertAndSendToUser(player, "/queue/reply",//
                        new Resp("stepEnd").fill("step", step).fill("nextStep", nextStep));
            }
        }
    }
}
