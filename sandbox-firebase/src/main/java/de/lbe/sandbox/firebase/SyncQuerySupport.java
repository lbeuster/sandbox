package de.lbe.sandbox.firebase;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * @author lbeuster
 */
public class SyncQuerySupport {

	private long writeTimeoutInMillis = 1000;

	private long readTimeoutInMillis = 1000;

	/**
	 *
	 */
	public <T> Map<String, T> query(Query query, Class<T> type) throws InterruptedException, TimeoutException {
		CountDownLatch sync = new CountDownLatch(1);
		Map<String, T> queryResult = new LinkedHashMap<>();
		query.addListenerForSingleValueEvent(new ValueEventListenerAdapter().onDataChange(data -> {
			if (data.exists()) {
				for (DataSnapshot child : data.getChildren()) {
					queryResult.put(child.getKey(), child.getValue(type));
				}
			}
			sync.countDown();
		}));
		await(sync, readTimeoutInMillis, query.getRef());
		return queryResult;
	}

	/**
	 *
	 */
	public boolean exists(DatabaseReference ref) throws InterruptedException, TimeoutException {

		// prepare
		CountDownLatch sync = new CountDownLatch(1);
		AtomicBoolean exists = new AtomicBoolean(false);

		// register a one shot listener
		ref.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListenerAdapter().onDataChange(data -> {
			exists.set(data.exists());
			sync.countDown();
		}));

		// wait for the result
		await(sync, readTimeoutInMillis, ref);
		return exists.get();
	}

	/**
	 *
	 */
	public void setValue(DatabaseReference ref, Object value) throws InterruptedException, TimeoutException {
		CountDownLatch sync = new CountDownLatch(1);
		ref.setValue(value, new CompletionListenerAdapter().onSuccess(_ref -> sync.countDown()));
		await(sync, writeTimeoutInMillis, ref);
	}

	/**
	 *
	 */
	public void updateChildren(DatabaseReference ref, Map<String, Object> updates) throws InterruptedException, TimeoutException {
		CountDownLatch sync = new CountDownLatch(1);
		ref.updateChildren(updates, new CompletionListenerAdapter().onSuccess(_ref -> sync.countDown()));
		await(sync, writeTimeoutInMillis, ref);
	}

	/**
	 *
	 */
	public void removeValue(DatabaseReference ref, long timeoutInMillis) throws InterruptedException, TimeoutException {
		CountDownLatch sync = new CountDownLatch(1);
		ref.removeValue(new CompletionListenerAdapter().onSuccess(_ref -> sync.countDown()));
		await(sync, timeoutInMillis, ref);
	}

	/**
	 *
	 */
	public void removeValue(DatabaseReference ref) throws InterruptedException, TimeoutException {
		removeValue(ref, writeTimeoutInMillis);
	}

	/**
	 *
	 */
	private void await(CountDownLatch sync, long timeoutInMillis, DatabaseReference ref) throws InterruptedException, TimeoutException {
		if (!sync.await(timeoutInMillis, TimeUnit.MILLISECONDS)) {
			throw new TimeoutException("ref=" + ref.getPath());
		}
	}
}
