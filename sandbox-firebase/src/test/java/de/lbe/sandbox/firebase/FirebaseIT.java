package de.lbe.sandbox.firebase;

import static de.asideas.ipool.commons.lib.test.hamcrest.Matchers.mapWithSize;
import static org.hamcrest.Matchers.hasKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ThrowOnExtraProperties;

import de.lbe.sandbox.firebase.ChildEventListenerAdapter;
import de.lbe.sandbox.firebase.FirebaseClient;
import de.lbe.sandbox.firebase.SyncQuerySupport;

/**
 * @author lbeuster
 */
public class FirebaseIT extends AbstractFirebaseIT {

	private SyncQuerySupport syncQueries;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception {
		this.syncQueries = new SyncQuerySupport();
	}

	/**
	 *
	 */
	@Test
	public void testSetValueOverwritesOldValue() throws Exception {

		// prepare
		DatabaseReference node = firebase.getReference(getTestMethodName());
		syncQueries.setValue(node, Collections.singletonMap("key1", new TestEntity("Montag", "Montag")));

		// add another node
		syncQueries.setValue(node, Collections.singletonMap("key2", new TestEntity("Dienstag", "Dienstag")));

		// query
		Map<String, TestEntity> entities = syncQueries.query(node.orderByKey(), TestEntity.class);
		assertThat(entities, mapWithSize(1));
		assertThat(entities, hasKey("key2"));
	}

	/**
	 *
	 */
	@Test
	public void testUpdateExistingValue() throws Exception {

		// prepare
		DatabaseReference node = firebase.getReference(getTestMethodName());
		syncQueries.setValue(node, Collections.singletonMap("key1", new TestEntity("Montag", "Dienstag")));

		// update
		Map<String, Object> update = new HashMap<>();
		update.put("key1/value1", "Mittwoch");
		syncQueries.updateChildren(node, update);

		// query
		Map<String, TestEntity> entities = syncQueries.query(node.orderByKey(), TestEntity.class);
		assertThat(entities, mapWithSize(1));
		TestEntity updatedEntity = entities.get("key1");
		assertNotNull(updatedEntity);
		assertEquals("Mittwoch", updatedEntity.getValue1());
		assertEquals("Dienstag", updatedEntity.getValue2());

	}

	/**
	 *
	 */
	@Test
	public void testUpdateNonExistingValue() throws Exception {

		DatabaseReference node = firebase.getReference(getTestMethodName());

		// if the key doesn't exists a new one is created
		Map<String, Object> update = new HashMap<>();
		update.put("key1/value1", "Mittwoch");
		syncQueries.updateChildren(node, update);

		// query
		Map<String, TestEntity> entities = syncQueries.query(node.orderByKey(), TestEntity.class);
		assertThat(entities, mapWithSize(1));
		TestEntity updatedEntity = entities.get("key1");
		assertNotNull(updatedEntity);
		assertEquals("Mittwoch", updatedEntity.getValue1());
		assertNull(updatedEntity.getValue2());
	}

	/**
	 *
	 */
	@Test
	public void testRegisterOnNonExistingNode() throws Exception {

		// we register a listener on a non-existing node
		DatabaseReference node = firebase.getReference(getTestMethodName());
		Map<String, Object> eventValue = new HashMap<>();
		node.addChildEventListener(new ChildEventListenerAdapter().onChildAdded((data, param) -> {
			@SuppressWarnings("unchecked")
			Map<String, Object> value = (Map<String, Object>) data.getValue();
			String key = data.getKey();
			eventValue.put(key, value);
		}));

		// now we create a key under the node
		syncQueries.setValue(node, Collections.singletonMap("key1", Collections.singletonMap("value1", "monday")));

		// assert the events
		assertThat(eventValue, mapWithSize(1));
		assertThat(eventValue, hasKey("key1"));
	}

	/**
	 *
	 */
	@Test
	public void testCreate2FirebaseInstances() throws Exception {
		FirebaseOptions options = newFirebaseOptions();
		FirebaseClient client1 = new FirebaseClient(options, getTestMethodName() + ".1");
		FirebaseClient client2 = new FirebaseClient(options, getTestMethodName() + ".2");

		client1.awaitFirstCompletion();
		client2.awaitFirstCompletion();
	}

	/**
	 *
	 */
	@ThrowOnExtraProperties
	static class TestEntity {

		private final String value1;

		private final String value2;

		public TestEntity(String value1, String value2) {
			this.value1 = value1;
			this.value2 = value2;
		}

		@SuppressWarnings("unused")
		private TestEntity() {
			// needed by firebase
			this(null, null);
		}

		public String getValue1() {
			return value1;
		}

		public String getValue2() {
			return value2;
		}
	}
}
