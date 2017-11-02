package com.yz.zwzb.service;

import com.yz.zwzb.domain.Match;
import com.yz.zwzb.domain.Room;
import com.yz.zwzb.domain.enums.RoomStatusEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Service
{
    static AtomicLong maxRoomId = new AtomicLong(1);

    static ConcurrentHashMap<Long, Room> rooms = new ConcurrentHashMap();

    public static Long createRoom()
    {
        long l = maxRoomId.addAndGet(1);
        rooms.putIfAbsent(l,new Room(l));
        return l;
    }

    public static Room joinRoom(Long roomId, Long playerId)
    {
        Room room = rooms.get(roomId);
        if(room == null){
            return  null;
        }
        HashSet<Long> playerIds = room.getPlayerIds();
        if (playerIds.size() >= room.getMaxPlayerNum())
        {
            return null;
        }
        if (!playerIds.contains(playerId))
        {
            playerIds.add(playerId);
            if(playerIds.size()>=room.getMaxPlayerNum()){
                room.setStatus(RoomStatusEnum.fighting);
                Match match = MatchService.newMatch();
                match.setPlayers(new ArrayList(room.getPlayerIds()));
                room.setMatchId(match.getId());
            }
        }
        else
        {
            return null;
        }
        return room;
    }
}
