package com.capstone.FileSharing.handler;

import com.capstone.FileSharing.model.SignalingMessage;
import com.capstone.FileSharing.model.TransferSession;
import com.capstone.FileSharing.service.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;
@Component
public class SignalingHandler extends TextWebSocketHandler {

    private final SessionService sessionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SignalingHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Connection opened
        System.out.println("WebSocket connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse incoming message
        SignalingMessage signalingMessage = objectMapper.readValue(message.getPayload(), SignalingMessage.class);
        String code = signalingMessage.getCode();
        TransferSession transferSession = sessionService.getSession(code);

        if (transferSession == null) {
            // Create new session if not exists
            transferSession = new TransferSession(code);
            sessionService.addSession(code, transferSession);
        }

        // Assign sender or receiver
        if (transferSession.getSender() == null) {
            transferSession.setSender(session);
        } else if (transferSession.getReceiver() == null && !Objects.equals(transferSession.getSender(), session)) {
            transferSession.setReceiver(session);
        }

        // Relay message to the other peer
        WebSocketSession target = transferSession.getSender() == session ? transferSession.getReceiver() : transferSession.getSender();
        if (target != null && target.isOpen()) {
            target.sendMessage(new TextMessage(message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove session if either peer disconnects
        sessionService.removeSessionBySocket(session);
        System.out.println("WebSocket disconnected: " + session.getId());
    }
}
