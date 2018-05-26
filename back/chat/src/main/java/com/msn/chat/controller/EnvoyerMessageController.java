package com.msn.chat.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.msn.chat.message.Message;
import com.msn.chat.message.MessageType;
import com.msn.chat.payload.ApiResponse;
import com.msn.chat.payload.SendMessageRequest;

@RestController
public class EnvoyerMessageController {

	@PostMapping("/api/chat/envoyer-message")
	public ResponseEntity<?> envoyerMessage(@Valid @RequestBody SendMessageRequest message) {

		if (Message.send(message.getMessage(),
				new MessageType(message.getDestinataire(), message.getEmetteur()).ampqRoutingKey())) {
			return ResponseEntity.ok(new ApiResponse(true, "Message envoyé"));
		}

		return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Message non envoyé"), HttpStatus.BAD_REQUEST);
	}

}
