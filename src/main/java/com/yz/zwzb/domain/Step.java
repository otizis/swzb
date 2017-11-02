package com.yz.zwzb.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Step
{
    Date time;
    String question;
    List<String> options;
    int answer;
    HashMap<Long,Result> playerAnswer = new HashMap<>();

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public List<String> getOptions()
    {
        return options;
    }

    public void setOptions(List<String> options)
    {
        this.options = options;
    }

    public int getAnswer()
    {
        return answer;
    }

    public void setAnswer(int answer)
    {
        this.answer = answer;
    }

    public HashMap<Long, Result> getPlayerAnswer()
    {
        return playerAnswer;
    }

    public void setPlayerAnswer(HashMap<Long, Result> playerAnswer)
    {
        this.playerAnswer = playerAnswer;
    }
}
