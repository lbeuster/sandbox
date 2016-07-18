package de.lbe.sandbox.firebase;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unfortunately we cannot access the default executor that executes the async firebase operations. It's possible the the executor queue grows faster than the operations can be
 * executed. In that case this class helps the block the threads that try to schedule new tasks.
 *
 * @author lbeuster
 */
public class BlockingExecutorSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlockingExecutorSupport.class);

	/**
	 * We use the client because the queue is created lazy.
	 */
	private int maxQueueSize = 250;

	private int minSleepInMillis = 500;

	private int maxSleepInMillis = 1000;

	private final Random random = new Random();

	/**
	 *
	 */
	@SuppressWarnings("boxing")
	public void blockUntilQueueIsReady(FirebaseClient client, Runnable runnable) throws InterruptedException {

		// we need this to ensure that we have the queue
		client.startIfNecessary();

		// wait until the queue is ready
		int millis = 0;
		while (true) {
			if (client.getExecutor().getQueue().size() < maxQueueSize) {
				break;
			}
			millis += sleep();
		}
		if (millis > 0) {
			LOGGER.info("Had to wait {} ms for queue to become ready", millis);
		}

		// execute
		runnable.run();
	}

	private int sleep() throws InterruptedException {
		int millis = random.nextInt(maxSleepInMillis - minSleepInMillis) + minSleepInMillis;
		Thread.sleep(millis);
		return millis;
	}

	public BlockingExecutorSupport maxQueueSize(int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
		return this;
	}

	public BlockingExecutorSupport minSleepInMillis(int minSleepInMillis) {
		this.minSleepInMillis = minSleepInMillis;
		return this;
	}

	public BlockingExecutorSupport maxSleepInMillis(int maxSleepInMillis) {
		this.maxSleepInMillis = maxSleepInMillis;
		return this;
	}
}
