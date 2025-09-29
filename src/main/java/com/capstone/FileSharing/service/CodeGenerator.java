package com.capstone.FileSharing.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;  // Example: 6-character codes
    private final SecureRandom random = new SecureRandom();

    // Generate a random unique code
    public String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
