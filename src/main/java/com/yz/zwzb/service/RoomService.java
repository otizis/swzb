package com.yz.zwzb.service;

import com.yz.zwzb.domain.Match;
import com.yz.zwzb.domain.Room;
import com.yz.zwzb.domain.enums.RoomStatusEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RoomService
{
    static AtomicLong maxRoomId = new AtomicLong(1);

    static ConcurrentHashMap<Long, Room> rooms = new ConcurrentHashMap<Long, Room>();

    public static Long createRoom()
    {
        long l = maxRoomId.addAndGet(1);
        rooms.putIfAbsent(l,new Room(l));
        return l;
    }

    public static Room joinRoom(Long roomId, String playerAccount)
    {
        Room room = rooms.get(roomId);
        if(room == null){
            return  null;
        }
        HashSet<String> playerAccounts = room.getPlayerAccounts();
        if (playerAccounts.size() >= room.getMaxPlayerNum())
        {
            return null;
        }
        if (!playerAccounts.contains(playerAccount))
        {
            playerAccounts.add(playerAccount);
            if(playerAccounts.size()>=room.getMaxPlayerNum()){
                room.setStatus(RoomStatusEnum.fighting);
                Match match = MatchService.newMatch();
                match.setPlayerAccounts(new ArrayList(room.getPlayerAccounts()));
                room.setMatchId(match.getId());
            }
        }
        else
        {
            return null;
        }
        return room;
    }
    public static Room exitRoom(Long roomId, String playerAccount)
    {
        Room room = rooms.get(roomId);
        if(room == null){
            return  null;
        }
        if(RoomStatusEnum.fighting.equals(room.getStatus())){
            return room;
        }
        HashSet<String> playerAccounts = room.getPlayerAccounts();
        playerAccounts.remove(playerAccount);
        return null;
    }

    public static Room getRoom(Long roomId)
    {
        return rooms.get(roomId);
    }
}
