package fr.ecervo.tchat.backend.backend.ws;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import fr.ecervo.tchat.backend.backend.models.Message;

public class WebSocketHandler extends TextWebSocketHandler {

    private static Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        broadast(session , Message.load(message.getPayload()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    /**
     * Dispatche les messages aux autres clients 
     * @param session
     * @param message
     */
    private static void broadast(WebSocketSession session, Message message) {
        sessions.stream()
            .filter(u -> !u.equals(session))
            .forEach(u -> {
                synchronized (u) {
                    try {
                        u.sendMessage(new TextMessage(message.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

}
