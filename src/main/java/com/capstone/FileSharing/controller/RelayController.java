package com.capstone.FileSharing.controller;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/relay")
public class RelayController {

    // Store in-memory file streams temporarily by code
    private final Map<String, Flux<DataBuffer>> fileStreams = new ConcurrentHashMap<>();

    // Upload file to a relay session
    @PostMapping(value = "/{code}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFile(@PathVariable String code, @RequestPart("file") FilePart filePart) {
        Flux<DataBuffer> dataBufferFlux = filePart.content().cache(); // cache to allow multiple subscribers
        fileStreams.put(code, dataBufferFlux);
        return Mono.just("File uploaded to session: " + code);
    }

    // Download file from relay session
    @GetMapping(value = "/{code}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<DataBuffer> downloadFile(@PathVariable String code) {
        Flux<DataBuffer> dataBufferFlux = fileStreams.get(code);
        if (dataBufferFlux == null) {
            return Flux.error(new RuntimeException("No file found for code: " + code));
        }
        // Remove the stream after downloading to avoid memory leak
        fileStreams.remove(code);
        return dataBufferFlux.doFinally(signal -> DataBufferUtils.release((DataBuffer) dataBufferFlux));
    }
}
