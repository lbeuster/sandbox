package de.lbe.sandbox.spring.retry;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class TestService {

	@Retryable(include = NullPointerException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, maxDelay = 3000, multiplier = 2))
	public void service() {
		System.out.println("service");
		throw new RuntimeException(new RemoteAccessException("TEST"));
	}

	@Recover
	public void recover(Exception e) throws Exception {
		throw e;
	}
}
