package com.yz.zwzb.service;

import com.yz.zwzb.domain.Match;
import com.yz.zwzb.domain.PlayerResultOneMatch;
import com.yz.zwzb.domain.Result;
import com.yz.zwzb.domain.Step;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class MatchService
{

    static AtomicLong maxRoomId = new AtomicLong(0);

    static HashMap<Long, Match> data = new HashMap<>();

    static List<Step> stepLib = new ArrayList<Step>();

    static
    {
        stepLib.add(newStep());
        stepLib.add(newStep1());
        stepLib.add(newStep2());
        stepLib.add(newStep3());
        stepLib.add(newStep4());
    }
    public static Match getMatch(Long matchId)
    {
        return data.get(matchId);
    }


    public static Match newMatch()
    {
        long l = maxRoomId.addAndGet(1);
        Match match = new Match();
        match.setId(l);
        match.setStartTime(new Date());
        match.setCurrStep(0);
        match.setSteps(get5Step());
        data.put(l, match);
        return match;
    }


    public static Step newStep()
    {
        Step step = new Step();
        step.setQuestion("‘落花岂是无情物’中哪个字错了？");
        step.setAnswer(0);
        step.setScore(5);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("落");
        step.getOptions().add("花");
        step.getOptions().add("岂");
        step.getOptions().add("物");
        return step;
    }

    public static Step newStep1()
    {
        Step step = new Step();
        step.setQuestion("‘一岁一枯荣’中哪个字错了？");
        step.setAnswer(1);
        step.setScore(10);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("一");
        step.getOptions().add("岁");
        step.getOptions().add("哭");
        step.getOptions().add("荣");
        return step;
    }

    public static Step newStep2()
    {
        Step step = new Step();
        step.setQuestion("‘化做春泥更护花’中哪个字错了？");
        step.setAnswer(2);
        step.setScore(15);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("画");
        step.getOptions().add("做");
        step.getOptions().add("春");
        step.getOptions().add("泥");
        return step;
    }

    public static Step newStep3()
    {
        Step step = new Step();
        step.setQuestion("‘白日依山尽’中哪个字错了？");
        step.setAnswer(3);
        step.setScore(15);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("百");
        step.getOptions().add("荣");
        step.getOptions().add("日");
        step.getOptions().add("尽");
        return step;
    }

    public static Step newStep4()
    {
        Step step = new Step();
        step.setQuestion("‘一语成谶’中最后一个字读作ji？");
        step.setAnswer(1);
        step.setScore(20);
        step.setOptions(new ArrayList<String>());
        step.getOptions().add("对");
        step.getOptions().add("错");
        return step;
    }

    public static void countResult(Match match)
    {
        List<Step> steps = match.getSteps();
        HashMap<String, PlayerResultOneMatch> matchResult = new HashMap<>();
        PlayerResultOneMatch win = null;
        for (Step step : steps)
        {
            HashMap<String, Result> playerAnswer = step.getPlayerAnswer();
            Set<Map.Entry<String, Result>> entries = playerAnswer.entrySet();
            Iterator<Map.Entry<String, Result>> iterator = entries.iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, Result> next = iterator.next();
                String account = next.getKey();
                Result result = next.getValue();
                PlayerResultOneMatch playerResultOneMatch = matchResult.get(account);
                if (playerResultOneMatch == null)
                {
                    playerResultOneMatch = new PlayerResultOneMatch();
                    matchResult.put(account, playerResultOneMatch);
                }
                int score = playerResultOneMatch.getScore();
                score += (result.isRight() ? step.getScore() : 0);
                playerResultOneMatch.setScore(score);
                if(win==null || score > win.getScore()){
                    win = playerResultOneMatch;
                }
            }
        }
        win.setWin(true);
        match.setMatchResult(matchResult);
    }

    public static List<Step> get5Step()
    {
        ArrayList<Step> steps = new ArrayList<>();
        int num = stepLib.size();
        Date now = new Date();
        for (int i = 0; i < 5; i++)
        {
            int randomInt = getRandomInt(1, num);
            Step step = stepLib.get(randomInt - 1);
            Step cloneStep = new Step();
            BeanUtils.copyProperties(step, cloneStep);
            cloneStep.setPlayerAnswer(new HashMap<String, Result>());
            cloneStep.setTime(now);
            steps.add(cloneStep);
        }
        return steps;
    }

    /**
     * 获得一个[min, max]之间的随机整数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInt(int min, int max)
    {
        return getRandom().nextInt(max - min + 1) + min;
    }

    //双重校验锁获取一个Random单例
    public static ThreadLocalRandom getRandom()
    {
        return ThreadLocalRandom.current();
    }
}
