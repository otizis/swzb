package com.yz.zwzb.domain;

import com.yz.zwzb.domain.enums.RoomStatusEnum;

import java.util.HashSet;

public class Room
{

    public Room()
    {
    }
    public Room(Long id)
    {
        this.id = id;
        this.status = RoomStatusEnum.waiting;
    }

    Long id;
    RoomStatusEnum status;
    Long matchId;
    HashSet<String> playerAccounts = new HashSet<>();
    int maxPlayerNum = 2;

    public int getMaxPlayerNum()
    {
        return maxPlayerNum;
    }

    public void setMaxPlayerNum(int maxPlayerNum)
    {
        this.maxPlayerNum = maxPlayerNum;
    }

    public HashSet<String> getPlayerAccounts()
    {
        return playerAccounts;
    }

    public void setPlayerAccounts(HashSet<String> playerAccounts)
    {
        this.playerAccounts = playerAccounts;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public RoomStatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(RoomStatusEnum status)
    {
        this.status = status;
    }

    public Long getMatchId()
    {
        return matchId;
    }

    public void setMatchId(Long matchId)
    {
        this.matchId = matchId;
    }
}
