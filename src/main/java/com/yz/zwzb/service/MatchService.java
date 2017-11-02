package com.yz.zwzb.service;

import com.yz.zwzb.domain.Match;
import com.yz.zwzb.domain.Step;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MatchService
{

    private static SimpMessagingTemplate template;

    static AtomicLong maxRoomId = new AtomicLong(1);

    static HashMap<Long,Match> data = new HashMap<>();

    public static Match getMatch(Long matchId){
        return data.get(matchId);
    }
    public static Match newMatch(){
        long l = maxRoomId.addAndGet(1);
        Match match = new Match();
        match.setId(l);
        match.setStartTime(new Date());
        match.setCurrStep(0);
        match.setSteps(new ArrayList<Step>());
        match.getSteps().add(newStep());
        match.getSteps().add(newStep1());
        match.getSteps().add(newStep2());
        match.getSteps().add(newStep3());
        match.getSteps().add(newStep4());
        data.put(l,match);
        return match;
    }

    public static Step newStep(){
        Step step = new Step();
        step.setQuestion("‘落花岂是无情物’中哪个字错了？");
        step.setAnswer(0);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("落");
        step.getOptions().add("花");
        step.getOptions().add("岂");
        step.getOptions().add("物");
        step.setTime(new Date());
        return step;
    }
    public static Step newStep1(){
        Step step = new Step();
        step.setQuestion("‘一岁一枯荣’中哪个字错了？");
        step.setAnswer(1);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("一");
        step.getOptions().add("岁");
        step.getOptions().add("哭");
        step.getOptions().add("荣");
        step.setTime(new Date());
        return step;
    }
    public static Step newStep2(){
        Step step = new Step();
        step.setQuestion("‘化做春泥更护花’中哪个字错了？");
        step.setAnswer(2);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("画");
        step.getOptions().add("做");
        step.getOptions().add("春");
        step.getOptions().add("泥");
        step.setTime(new Date());
        return step;
    }
    public static Step newStep3(){
        Step step = new Step();
        step.setQuestion("‘白日依山尽’中哪个字错了？");
        step.setAnswer(3);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("百");
        step.getOptions().add("荣");
        step.getOptions().add("日");
        step.getOptions().add("尽");
        step.setTime(new Date());
        return step;
    }
    public static Step newStep4(){
        Step step = new Step();
        step.setQuestion("‘一语成谶’中最后一个字读作ji？");
        step.setAnswer(1);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("对");
        step.getOptions().add("错");
        step.setTime(new Date());
        return step;
    }

}
