package com.yz.zwzb.domain;

import java.util.Date;
import java.util.List;

public class Match
{
    Long id;
    Date startTime;
    List<Long> players;
    List<Step> steps;
    int currStep;

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

    public List<Long> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Long> players)
    {
        this.players = players;
    }

    public List<Step> getSteps()
    {
        return steps;
    }

    public void setSteps(List<Step> steps)
    {
        this.steps = steps;
    }
}
