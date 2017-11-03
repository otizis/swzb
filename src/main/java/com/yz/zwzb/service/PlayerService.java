package com.yz.zwzb.service;

import com.yz.zwzb.domain.Player;

import java.util.*;

public class PlayerService
{
    static HashMap<String, Player> onlinePlayer = new HashMap<>();

    public static void add(String account)
    {
        Player player = new Player();
        player.setAccount(account);
        onlinePlayer.put(account, player);
    }

    public static void updatePlayerRoomId(String account, Long roomId)
    {
        onlinePlayer.get(account).setRoomId(roomId);
    }

    public static List<Player> getPlayerOutRoom()
    {
        ArrayList<Player> players = new ArrayList<>();
        Set<Map.Entry<String, Player>> entries = onlinePlayer.entrySet();
        Iterator<Map.Entry<String, Player>> iterator = entries.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, Player> next = iterator.next();
            if (next.getValue().getRoomId() == null)
            {
                players.add(next.getValue());
            }
        }
        return players;
    }

    public static void remove(String account)
    {
        onlinePlayer.remove(account);
    }
}
