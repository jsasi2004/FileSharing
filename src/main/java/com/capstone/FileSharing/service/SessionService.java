package com.capstone.FileSharing.service;

import com.capstone.FileSharing.model.TransferSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    // In-memory storage of active sessions by code
    private final Map<String, TransferSession> sessions = new ConcurrentHashMap<>();

    // Add a new session
    public void addSession(String code, TransferSession session) {
        sessions.put(code, session);
    }

    // Get session by code
    public TransferSession getSession(String code) {
        return sessions.get(code);
    }

    // Remove session by code
    public void removeSession(String code) {
        sessions.remove(code);
    }

    // Remove session if a WebSocket disconnects
    public void removeSessionBySocket(WebSocketSession socket) {
        sessions.entrySet().removeIf(entry ->
                (entry.getValue().getSender() != null && entry.getValue().getSender().equals(socket)) ||
                        (entry.getValue().getReceiver() != null && entry.getValue().getReceiver().equals(socket))
        );
    }

    // Store uploaded file in memory
    public void storeFile(String code, MultipartFile file) throws Exception {
        TransferSession session = sessions.getOrDefault(code, new TransferSession(code));
        session.setCode(code);
        session.setFile(file.getBytes());
        session.setFilename(file.getOriginalFilename());
        sessions.put(code, session);
    }
}
