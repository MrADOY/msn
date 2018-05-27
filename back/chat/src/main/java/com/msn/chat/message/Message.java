package com.msn.chat.message;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
public class Message {
	

	private String addr;
	private int port;
	private static String exchange_name = "bus-messages";

	private Connection connection;
	private static Channel channel;

	@Autowired
	public Message(@Value("${rabbit.addr}") String addr,
				   @Value("${rabbit.port}") int port) {
		ConnectionFactory factory = new ConnectionFactory();
		this.addr = addr;
		this.port = port;
		factory.setHost(this.addr);
		factory.setPort(this.port);
		try {
			connection = factory.newConnection();
			Message.channel = connection.createChannel();
			Message.channel.exchangeDeclare(exchange_name, "topic");
		} catch (IOException | TimeoutException e) {
			System.err.println("unable to connect to rabbitmq");
			System.exit(-1);
		}
	}

	public static boolean send(String message, String routingKey) {
		try {
			Message.channel.basicPublish(Message.exchange_name, routingKey, null, message.getBytes());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public static Channel getChannel() {
		return channel;
	}

	public static void setChannel(Channel channel) {
		Message.channel = channel;
	}

	public static String getExchange_name() {
		return exchange_name;
	}

	public static void setExchange_name(String exchange_name) {
		Message.exchange_name = exchange_name;
	}
}
