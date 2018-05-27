package com.msn.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.msn.chat.config.ProprietesSauvegardeFichier;

@SpringBootApplication
@EnableConfigurationProperties({
	ProprietesSauvegardeFichier.class
})
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}
}
