package com.yz.zwzb.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Match
{
    Long id;
    Date startTime;
    List<String> playerAccounts;
    List<Step> steps;
    int currStep;
    HashMap<String,PlayerResultOneMatch> matchResult;
    String result;

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public int getCurrStep()
    {
        return currStep;
    }

    public void setCurrStep(int currStep)
    {
        this.currStep = currStep;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public List<String> getPlayerAccounts()
    {
        return playerAccounts;
    }

    public void setPlayerAccounts(List<String> playerAccounts)
    {
        this.playerAccounts = playerAccounts;
    }

    public List<Step> getSteps()
    {
        return steps;
    }

    public void setSteps(List<Step> steps)
    {
        this.steps = steps;
    }

    public HashMap<String, PlayerResultOneMatch> getMatchResult()
    {
        return matchResult;
    }

    public void setMatchResult(HashMap<String, PlayerResultOneMatch> matchResult)
    {
        this.matchResult = matchResult;
    }
}
