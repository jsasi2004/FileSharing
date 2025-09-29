package com.capstone.FileSharing.model;

import org.springframework.web.socket.WebSocketSession;

public class TransferSession {

    private String code;                     // Unique session code
    private WebSocketSession sender;         // Sender WebSocket
    private WebSocketSession receiver;       // Receiver WebSocket

    public TransferSession(String code) {
        this.code = code;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public WebSocketSession getSender() {
        return sender;
    }

    public void setSender(WebSocketSession sender) {
        this.sender = sender;
    }

    public WebSocketSession getReceiver() {
        return receiver;
    }

    public void setReceiver(WebSocketSession receiver) {
        this.receiver = receiver;
    }
}
