package com.yz.zwzb.events;

import com.yz.zwzb.domain.Player;
import com.yz.zwzb.domain.response.Resp;
import com.yz.zwzb.service.PlayerService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Configuration
public class StompConnectEvent implements ApplicationListener<SessionConnectEvent>
{

    @Resource
    private SimpMessagingTemplate template;

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent)
    {
        Principal user = sessionConnectEvent.getUser();
        PlayerService.add(user.getName());
        List<Player> playerOutRoom = PlayerService.getPlayerOutRoom();
        template.convertAndSend("/topic", new Resp("msg").fill("text", user.getName() + "上线了,可邀请用户：" + playerOutRoom
                .size()));
    }
}
