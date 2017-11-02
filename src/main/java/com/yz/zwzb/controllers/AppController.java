package com.yz.zwzb.controllers;

import com.yz.zwzb.domain.Match;
import com.yz.zwzb.domain.Result;
import com.yz.zwzb.domain.Room;
import com.yz.zwzb.domain.Step;
import com.yz.zwzb.domain.enums.RoomStatusEnum;
import com.yz.zwzb.domain.request.CreateRoomReq;
import com.yz.zwzb.domain.response.Resp;
import com.yz.zwzb.service.MatchService;
import com.yz.zwzb.service.Service;
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
    public void createRoom(Principal principal, CreateRoomReq req)
    {
        String name = principal.getName();
        System.out.println("name :" + name);
        Long roomId = Service.createRoom();
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
        Resp message = new Resp("answer");
        String matchId = req.get("matchId").toString();
        Integer answer = (Integer)req.get("answer");
        Long matchIdLong = Long.parseLong(matchId);
        Match match = MatchService.getMatch(matchIdLong);
        List<Step> steps = match.getSteps();
        Step step = steps.get(match.getCurrStep());
        if(step!=null){
            HashMap<Long, Result> playerAnswer = step.getPlayerAnswer();

            playerAnswer.put(Long.parseLong(principal.getName()), new Result(answer).judg(step.getAnswer()));

            int size = playerAnswer.size();
            if(size == match.getPlayers().size()){
                // 环节结束，通知这道题目的所有人答题结果，下发下一道题目
                sendStepEnd(matchIdLong);
            }
        }
    }

    // 用户加入房间
    @MessageMapping("/joinRoom/{roomId}")
    public void joinRoom(Principal principal, @DestinationVariable Long roomId)
    {
        Long playerId = Long.parseLong(principal.getName());
        Room room = Service.joinRoom(roomId, playerId);

        template.convertAndSendToUser(principal.getName(), "/queue/reply", new Resp("joinRoom").fill("room", room));
        HashSet<Long> playerIds = room.getPlayerIds();
        Iterator<Long> iterator = playerIds.iterator();
        while (iterator.hasNext())
        {
            Long next = iterator.next();
            if (next != null && next.compareTo(playerId) != 0)
            {
                template.convertAndSendToUser(String.valueOf(next), "/queue/reply",//
                        new Resp("updateRoom").fill("room", room));
            }
        }
        if (RoomStatusEnum.fighting.equals(room.getStatus()))
        {
            sendStepBegin(room.getMatchId());
        }
    }

    private void sendStepBegin(Long matchId)
    {
        Match match = MatchService.getMatch(matchId);
        if (match != null)
        {
            List<Long> players = match.getPlayers();
            List<Step> steps = match.getSteps();
            int currStep = match.getCurrStep();
            Step step = steps.get(currStep);
            for (Long player : players)
            {
                template.convertAndSendToUser(String.valueOf(player), "/queue/reply",//
                        new Resp("stepBegin").fill("step", step));
            }
        }
    }
    private void sendStepEnd(Long matchId)
    {
        Match match = MatchService.getMatch(matchId);
        if (match != null)
        {
            List<Step> steps = match.getSteps();
            int currStep = match.getCurrStep();
            Step step = steps.get(currStep);
            Step nextStep = null;
            if(currStep < steps.size()-1){
                nextStep = steps.get(currStep+1);
            }
            List<Long> players = match.getPlayers();
            for (Long player : players)
            {
                template.convertAndSendToUser(String.valueOf(player), "/queue/reply",//
                        new Resp("stepEnd").fill("step", step).fill("nextStep", nextStep));
            }
            match.setCurrStep(currStep + 1);
        }
    }
}
