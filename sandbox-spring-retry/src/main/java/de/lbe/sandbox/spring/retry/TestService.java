package de.lbe.sandbox.spring.retry;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class TestService {

	@Retryable(include = RemoteAccessException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void service() {
		System.out.println("service");
		throw new RemoteAccessException("TEST");
	}

	@Recover
	public void recover(RemoteAccessException e) {
		System.out.println("recovered");
		// ... panic
	}
}
