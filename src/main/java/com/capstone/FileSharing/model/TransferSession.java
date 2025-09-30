package com.capstone.FileSharing.model;

import org.springframework.web.socket.WebSocketSession;

public class TransferSession {

    private String code;                     // Unique session code
    private WebSocketSession sender;         // Sender WebSocket
    private WebSocketSession receiver;       // Receiver WebSocket
    private String contentType;

    // For file transfer
    private byte[] file;                     // File content in bytes
    private String filename;                 // Original filename

    public TransferSession() {}

    public TransferSession(String code) {
        this.code = code;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
