package fr.ecervo.tchat.backend.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import fr.ecervo.tchat.backend.backend.models.Message;
/**
 * Tests du Websocket
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class WebSocketTests {

	private WebSocketClient sender;
	private WebSocketClient receiver;

	Message receivedByReceiver;
	Message receivedBySender;
	// verrou pour les assertions
	private CountDownLatch lock; 


	@BeforeAll
	void setup() {
		this.sender = new StandardWebSocketClient();
		this.receiver = new StandardWebSocketClient();
		this.lock = new CountDownLatch(2);
	}

	/**
	 * Methode pour la factorisation de la création de session
	 * @param client {@link WebSocketClient}
	 * @param handler {@link FunctionalInterface} pour retourner le message reçu par le serveur
	 * @return {@link WebSocketSession} la session d'un websocket coté client
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	private WebSocketSession session(WebSocketClient client, HandleMessage handler)
			throws InterruptedException, ExecutionException, TimeoutException {
		return client.doHandshake(
				new TextWebSocketHandler() {
					@Override
					protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
						handler.handle(Message.load(message.getPayload()));						
					}
				},
				new WebSocketHttpHeaders(), URI.create("ws://localhost:6699"))
				.get(10, TimeUnit.SECONDS);
	}




	/**
	 * Le test s'assure que le client qui envoie le message soit le seul a ne pas recevoir le message envoyé
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws JsonProcessingException
	 * @throws IOException
	 * @throws TimeoutException
	 */
	@Test
	void testWS()
			throws InterruptedException, ExecutionException, JsonProcessingException, IOException, TimeoutException {
		Message message = new Message("Loïc","Hello World");
	
		this.session(receiver, (m) -> {
				receivedByReceiver = m;
				lock.countDown();
			});

		this.session(sender, (m)->{
			receivedBySender = m;
			lock.countDown();
		}).sendMessage(new TextMessage(message.toString()));

		// Avec 2 secondes , libération si le sender n'a pas reçu de message
		lock.await(2,TimeUnit.SECONDS);
		assertEquals(message.username, receivedByReceiver.username);
		assertEquals(message.content, receivedByReceiver.content);
		assertNull(receivedBySender);

	}

	/**
	 * Callback pour remonter le message via la methode de session
	 */
	@FunctionalInterface
	interface HandleMessage {

		void handle(Message message);
	}

}
