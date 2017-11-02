package com.yz.zwzb.domain;

import org.thymeleaf.util.NumberUtils;
import org.thymeleaf.util.StringUtils;

import java.util.Date;

public class Result
{
    Long playerId;
    Date answerTime;
    Integer answer;
    boolean isRight;

    public Result judg(int rightAnswer){
        if(answer == null){
            this.isRight = false;
            return this;
        }
        this.isRight = answer.intValue() == rightAnswer;
        return this;
    }

    public Long getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(Long playerId)
    {
        this.playerId = playerId;
    }

    public boolean isRight()
    {
        return isRight;
    }

    public void setRight(boolean right)
    {
        isRight = right;
    }

    public Result(){

    }
    public Result(int answer){
        this.answer = answer;
    }
    public Date getAnswerTime()
    {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime)
    {
        this.answerTime = answerTime;
    }

    public Integer getAnswer()
    {
        return answer;
    }

    public void setAnswer(Integer answer)
    {
        this.answer = answer;
    }
}
