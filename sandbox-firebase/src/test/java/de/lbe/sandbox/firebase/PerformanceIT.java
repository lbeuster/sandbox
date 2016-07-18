package de.lbe.sandbox.firebase;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.firebase.database.DatabaseReference;

import de.lbe.sandbox.firebase.BlockingExecutorSupport;
import de.lbe.sandbox.firebase.FirebaseClient;
import de.lbe.sandbox.firebase.FirebaseClientPool;

/**
 * @author lbeuster
 */
public class PerformanceIT extends AbstractFirebaseIT {

	/**
	 *
	 */
	@Test
	public void testPerformance() throws Exception {

		FirebaseClientPool pool = new FirebaseClientPool(newFirebaseOptions(), "test", 1);
		pool.awaitFirstCompletion();

		BlockingExecutorSupport block = new BlockingExecutorSupport();
		AtomicInteger count = new AtomicInteger();
		AtomicLong bytes = new AtomicLong();
		long startTime = System.currentTimeMillis();
		while (true) {
			FirebaseClient client = pool.randomClient();
			DatabaseReference node = client.getReference(getTestMethodName() + "/larstest");
			block.blockUntilQueueIsReady(client, () -> {
				int length = 10_000;
				String payload = largeString(length);
				bytes.addAndGet(length);
				node.updateChildren(Collections.singletonMap(UUID.randomUUID().toString(), payload));
				count.incrementAndGet();
			});

			if (count.get() % 50 == 0) {
				long endTime = System.currentTimeMillis();
				long durationInSeconds = (endTime - startTime) / 1000;
				if (durationInSeconds == 0) {
					durationInSeconds = 1;
				}
				long messagesPerSecond = count.get() / durationInSeconds;
				long bytesPerSecond = bytes.get() / durationInSeconds;
				System.out.println("throughput: " + messagesPerSecond + "/sec, " + bytesPerSecond + " B/sec, count=" + count.get());
			}
		}
	}

	private String largeString(int length) {
		return StringUtils.repeat("a", length);
	}
}
