package com.desyd.api.util;

import com.desyd.api.repository.SessionRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SessionCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    private final SessionRepository sessionRepository;

    public SessionCodeGenerator(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public String generateUniqueCode() {
        String code;
        int attempts = 0;
        int maxAttempts = 10;
        do {
            code = generateCode();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("Failed to generate unique session code after " + maxAttempts + " attempts");
            }
        } while (sessionRepository.existsBySessionCode(code));
        return code;
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }
}