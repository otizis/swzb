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
    int score = 5;
    HashMap<String,Result> playerAnswer = new HashMap<>();

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

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

    public HashMap<String, Result> getPlayerAnswer()
    {
        return playerAnswer;
    }

    public void setPlayerAnswer(HashMap<String, Result> playerAnswer)
    {
        this.playerAnswer = playerAnswer;
    }
}
