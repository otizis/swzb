package com.yz.zwzb.domain.request;

public class CreateRoomReq
{
    Long playerId;

    public Long getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(Long playerId)
    {
        this.playerId = playerId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("CreateRoomReq{");
        sb.append("playerId=").append(playerId);
        sb.append('}');
        return sb.toString();
    }
}
