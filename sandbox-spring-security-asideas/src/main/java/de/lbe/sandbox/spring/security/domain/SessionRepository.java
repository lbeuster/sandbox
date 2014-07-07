package de.lbe.sandbox.spring.security.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class SessionRepository {

	private static final Map<String, String> validSessionIds = new HashMap<String, String>();

	public String createSessionId(String user) {
		String sessionId = UUID.randomUUID().toString();
		validSessionIds.put(sessionId, user);
		return sessionId;
	}

	public String getUserBySessionId(String sessionId) {
		return validSessionIds.get(sessionId);
	}
}