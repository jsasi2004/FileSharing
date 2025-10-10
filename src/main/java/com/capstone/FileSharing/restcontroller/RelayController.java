package com.capstone.FileSharing.restcontroller;

import com.capstone.FileSharing.config.security.CustomUserDetails;
import com.capstone.FileSharing.model.Logs;
import com.capstone.FileSharing.model.TransferSession;
import com.capstone.FileSharing.service.LogsService;
import com.capstone.FileSharing.service.SessionService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/relay")
@RequiredArgsConstructor
public class RelayController {

    private final SessionService sessionService;
    private final LogsService logsService;

    // @Autowired
    // public RelayController(SessionService sessionService) {
    //     this.sessionService = sessionService;
    // }

    // WebSocket sessions map (code -> TransferSession)
    private final Map<String, TransferSession> sessions = new ConcurrentHashMap<>();

    // Upload file to session
    @PostMapping("/{code}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable String code,
                                             @RequestParam("file") MultipartFile file,Authentication authentication) throws Exception {

        TransferSession session = sessions.computeIfAbsent(code, TransferSession::new);
        session.setFile(file.getBytes());
        session.setFilename(file.getOriginalFilename());
        session.setContentType(file.getContentType());

        Logs logs = new Logs();

        // Notify receiver if connected
        if (session.getReceiver() != null && session.getReceiver().isOpen()) {
            session.getReceiver().sendMessage(new TextMessage("{\"type\":\"FILE_READY\",\"code\":\"" + code + "\"}"));
        }
        var principal = authentication.getPrincipal();
        sessionService.addSession(code, session);
        CustomUserDetails use = (CustomUserDetails) principal;
        logs.setWho(use.getEmail());
        logs.setCode(code);
        logs.setAction("UPLOAD");
        logs.setFilename(file.getOriginalFilename());
        logs.setTime(new Date());
        logsService.saveLog(logs);

        return ResponseEntity.ok("File uploaded for session: " + code);
    }

    // Download file from session
    @GetMapping("/{code}/download")
    public ResponseEntity<?> downloadFile(@PathVariable String code,Authentication authentication) {
        TransferSession session = sessionService.getSession(code);

        if (session == null || session.getFile() == null) {
            return ResponseEntity.badRequest().build();
        }
        var principal = authentication.getPrincipal();
        Logs logs = new Logs();
        ByteArrayResource resource = new ByteArrayResource(session.getFile());
        logs.setWho(((CustomUserDetails) principal).getUsername());
        logs.setCode(code);
        logs.setAction("DOWNLOAD");
        logs.setTime(new Date());
        logs.setFilename(session.getFilename());
        logsService.saveLog(logs);
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
