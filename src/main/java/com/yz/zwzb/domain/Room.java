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
    HashSet<Long> playerIds = new HashSet<>();
    int maxPlayerNum = 1;

    public int getMaxPlayerNum()
    {
        return maxPlayerNum;
    }

    public void setMaxPlayerNum(int maxPlayerNum)
    {
        this.maxPlayerNum = maxPlayerNum;
    }

    public HashSet<Long> getPlayerIds()
    {
        return playerIds;
    }

    public void setPlayerIds(HashSet<Long> playerIds)
    {
        this.playerIds = playerIds;
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
