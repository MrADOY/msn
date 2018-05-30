package com.msn.log.message;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msn.log.modeles.Application;
import com.msn.log.modeles.Log;
import com.msn.log.modeles.TypeLog;
import com.msn.log.repertoire.RepertoiresLog;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class Message {
	
	
	/* Impossible "autowired" un attribut static, donc 
	 * on utilise un petit tour de magie :)
	 */
	private static RepertoiresLog repertoireLog;

	@Autowired
	private RepertoiresLog repertoireLogInit;

	@PostConstruct
	private void initStaticDao() {
		repertoireLog = this.repertoireLogInit;
	}

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
	
	public static boolean sendLogMessage(String message, TypeLog type,Application appli) {

		String logInJson = "";
		try {
			logInJson = new ObjectMapper().writeValueAsString(new Log(type,appli,message, new Date()));
		} catch (JsonProcessingException e1) {
			System.out.println("Erreur de parsing");
		}
		try {
			Message.channel.basicPublish(Message.exchange_name, "LOG.System", null, logInJson.getBytes());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static void recevoirMessageRabbitMQ() {

		String[] argv = { "LOG.#" };
		String queueName = null;
		try {
			queueName = channel.queueDeclare().getQueue();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (String bindingKey : argv) {
			try {
				channel.queueBind(queueName, exchange_name, bindingKey);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
			//	System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
				ObjectMapper mapper = new ObjectMapper();
				Log log = mapper.readValue(message, Log.class);
				log.setId(0L);
				repertoireLog.save(log);
			}
		};
		try {
			channel.basicConsume(queueName, true, consumer);
		} catch (IOException e) {
			System.out.println("test");
			e.printStackTrace();
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
