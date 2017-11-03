package com.yz.zwzb.events;

import com.yz.zwzb.service.PlayerService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

@Configuration
public class StompConnectEvent implements ApplicationListener<SessionConnectEvent>
{

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent)
    {
        Principal user = sessionConnectEvent.getUser();
        PlayerService.add(user.getName());
    }
}
