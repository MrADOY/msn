package com.msn.chat.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.msn.chat.message.Message;
import com.msn.chat.message.MessageType;
import com.msn.chat.payload.ApiResponse;
import com.msn.chat.payload.SendMessageRequest;

@RestController
public class EnvoyerMessageController {

	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Autowired
	RestTemplate restTemplate;
	/* Cette fonction permet d'envoyer un message à une personne qui doit ETRE CONNECTER*/

	@PostMapping("/api/chat/envoyer-message")
	public ResponseEntity<?> envoyerMessage(@Valid @RequestBody SendMessageRequest message) {

		if (isConnecte(message.getDestinataire())) {
			if (Message.send(message.getMessage(),
					new MessageType(message.getDestinataire(),message.getEmetteur()).ampqRoutingKey())) {
				return ResponseEntity.ok(new ApiResponse(true, "Message envoyé"));
			}
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Message non envoyé destinataire hors ligne"),
				HttpStatus.BAD_REQUEST);
	}
	
	public Boolean isConnecte(String email) {
		return restTemplate.getForObject("http://localhost:5000/api/connexions/connecte/" + email, Boolean.class);
	}
}
