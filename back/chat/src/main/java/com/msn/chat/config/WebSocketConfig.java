package com.msn.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {

	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		// Connexion à RabbitMQ

		registry.setApplicationDestinationPrefixes("/app")
				.enableStompBrokerRelay("/topic")
				.setRelayHost("localhost")
				.setRelayPort(61613)
				.setClientLogin("guest")
				.setClientPasscode("guest");
	}

}
