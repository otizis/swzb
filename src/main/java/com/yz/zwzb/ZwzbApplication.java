package com.yz.zwzb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class ZwzbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwzbApplication.class, args);
	}
}
