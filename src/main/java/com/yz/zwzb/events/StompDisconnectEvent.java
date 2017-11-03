package com.yz.zwzb.events;

import com.yz.zwzb.service.PlayerService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Configuration
public class StompDisconnectEvent implements ApplicationListener<SessionDisconnectEvent>
{


    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent)
    {
        Principal user = sessionDisconnectEvent.getUser();
        PlayerService.remove(user.getName());
    }
}
