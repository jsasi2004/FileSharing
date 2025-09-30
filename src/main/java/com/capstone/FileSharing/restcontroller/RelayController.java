package com.capstone.FileSharing.restcontroller;

import com.capstone.FileSharing.model.TransferSession;
import com.capstone.FileSharing.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/relay")
public class RelayController {

    private final SessionService sessionService;

    @Autowired
    public RelayController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // WebSocket sessions map (code -> TransferSession)
    private final Map<String, TransferSession> sessions = new ConcurrentHashMap<>();

    // Upload file to session
    @PostMapping("/{code}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable String code,
                                             @RequestParam("file") MultipartFile file) throws Exception {

        TransferSession session = sessions.computeIfAbsent(code, TransferSession::new);

        session.setFile(file.getBytes());
        session.setFilename(file.getOriginalFilename());
        session.setContentType(file.getContentType());

        // Notify receiver if connected
        if (session.getReceiver() != null && session.getReceiver().isOpen()) {
            session.getReceiver().sendMessage(new TextMessage("{\"type\":\"FILE_READY\",\"code\":\"" + code + "\"}"));
        }

        sessionService.addSession(code, session);

        return ResponseEntity.ok("File uploaded for session: " + code);
    }

    // Download file from session
    @GetMapping("/{code}/download")
    public ResponseEntity<?> downloadFile(@PathVariable String code) {
        TransferSession session = sessionService.getSession(code);

        if (session == null || session.getFile() == null) {
            return ResponseEntity.badRequest().build();
        }

        ByteArrayResource resource = new ByteArrayResource(session.getFile());

        // Optionally remove session after download
        sessionService.removeSession(code);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + session.getFilename())
                .header("filename", session.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(session.getFile().length)
                .body(resource);
    }
}
