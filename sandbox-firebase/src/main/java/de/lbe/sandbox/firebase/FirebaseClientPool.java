package de.lbe.sandbox.firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.firebase.FirebaseOptions;

/**
 * @author lbeuster
 */
public class FirebaseClientPool {

	private final List<FirebaseClient> clients = new ArrayList<>();

	private AtomicInteger loadbalancer = new AtomicInteger();

	public FirebaseClientPool(FirebaseOptions options, String namePrefix, int poolSize) {
		for (int i = 0; i < poolSize; i++) {
			String name = namePrefix + "-" + i;
			clients.add(new FirebaseClient(options, name));
		}
	}

	public void awaitFirstCompletion() throws InterruptedException, TimeoutException {
		for (FirebaseClient client : clients) {
			client.awaitFirstCompletion();
		}
	}

	public FirebaseClient randomClient() {
		int idx = loadbalancer.incrementAndGet() % clients.size();
		return clients.get(idx);
	}

	/**
	 *
	 */
	public FirebaseClient client() {

		// small optimization
		if (clients.size() == 1) {
			return clients.get(0);
		}

		// select the client with the least number of active runnables
		FirebaseClient result = null;
		for (FirebaseClient client : clients) {

			// initialize with the first in our list
			client.startIfNecessary();
			if (result == null) {
				result = client;
			} else {

				// if the new client a less active runnables
				if (client.getExecutor().getActiveCount() < result.getExecutor().getActiveCount()) {
					result = client;
				}
			}
		}
		return result;
	}
}
