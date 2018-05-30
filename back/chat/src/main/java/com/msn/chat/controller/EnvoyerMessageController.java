package com.msn.chat.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.msn.chat.message.Message;
import com.msn.chat.message.MessageType;
import com.msn.chat.modeles.Application;
import com.msn.chat.modeles.TypeLog;
import com.msn.chat.payload.ApiResponse;
import com.msn.chat.payload.MessageRecu;
import com.msn.chat.payload.SendMessageRequest;



@RestController
@CrossOrigin
public class EnvoyerMessageController {

	@Autowired
	private SimpMessagingTemplate webSocketMessagingTemplate;
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Autowired
	RestTemplate restTemplate;
	/*
	 * Cette fonction permet d'envoyer un message à une personne qui doit ETRE
	 * CONNECTER
	 */

	@PostMapping("/api/chat/envoyer-message")
	public ResponseEntity<?> envoyerMessage(@Valid @RequestBody SendMessageRequest message) {

		if (isConnecte(message.getDestinataire())) {
			if (Message.send(message.getMessage(),
					new MessageType(message.getDestinataire(), message.getEmetteur()).ampqRoutingKey())) {
				Message.sendLogMessage(message.getEmetteur() + "a envoyé un message à " + message.getDestinataire(),
						TypeLog.INFO, Application.CHAT);
				sendPrivateMessage(
						new MessageRecu(message.getDestinataire(), message.getEmetteur(), message.getMessage()));
				return ResponseEntity.ok(new ApiResponse(true, "Message envoyé"));

			}
		}
		Message.sendLogMessage(message.getEmetteur() + "n'a pas su envoyé son message à " + message.getDestinataire(),
				TypeLog.WARNING, Application.CHAT);
		return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Message non envoyé destinataire hors ligne"),
				HttpStatus.BAD_REQUEST);

	}

	public Boolean isConnecte(String email) {
		return restTemplate.getForObject("http://172.17.0.3:5000/api/connexions/connecte/" + email, Boolean.class);
	}
	
	public void sendPrivateMessage(MessageRecu message) {
		webSocketMessagingTemplate.convertAndSend(
				"/topic/" + message.getDestinataire() + ".public.messages",
				message);
	}
	
}


