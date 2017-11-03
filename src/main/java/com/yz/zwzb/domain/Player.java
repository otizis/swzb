package com.yz.zwzb.domain;

public class Player
{
    String account;
    String password;
    Long score;
    Long money;
    Long engine;
    Long roomId;

    public Long getRoomId()
    {
        return roomId;
    }

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Long getScore()
    {
        return score;
    }

    public void setScore(Long score)
    {
        this.score = score;
    }

    public Long getMoney()
    {
        return money;
    }

    public void setMoney(Long money)
    {
        this.money = money;
    }

    public Long getEngine()
    {
        return engine;
    }

    public void setEngine(Long engine)
    {
        this.engine = engine;
    }
}
