package de.lbe.sandbox.firebase;

import static de.asideas.ipool.commons.lib.test.hamcrest.Matchers.mapWithSize;
import static org.hamcrest.Matchers.hasKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ThrowOnExtraProperties;

import de.lbe.sandbox.firebase.SyncQuerySupport;

/**
 * @author lbeuster
 */
public class SyncQuerySupportIT extends AbstractFirebaseIT {

	private SyncQuerySupport support;

	/**
	 *
	 */
	@Before
	public void setUp() throws Exception {
		support = new SyncQuerySupport();
	}

	/**
	 *
	 */
	@Test
	public void testExistsOnNonExistingNode() throws Exception {
		DatabaseReference ref = firebase.getReference(getTestMethodName() + "_nonExisting");
		assertFalse(support.exists(ref));
	}

	/**
	 *
	 */
	@Test
	public void testExistsOnExistingNode() throws Exception {
		DatabaseReference ref = firebase.getReference(getTestMethodName());

		// we need a map - a single string as the value doesn't work
		support.setValue(ref, Collections.singletonMap("key", "value"));
		assertTrue(support.exists(ref));

		// cleanup
		ref.removeValue();
	}

	/**
	 *
	 */
	@Test
	public void testRemoveNonExistingValue() throws Exception {
		DatabaseReference ref = firebase.getReference(getTestMethodName() + "_nonExisting");
		assertFalse(support.exists(ref));
		support.removeValue(ref);
		assertFalse(support.exists(ref));
	}

	/**
	 *
	 */
	@Test
	public void testRemoveExistingValue() throws Exception {
		DatabaseReference ref = firebase.getReference(getTestMethodName() + "_nonExisting");
		support.setValue(ref, Collections.singletonMap("key", "value"));
		assertTrue(support.exists(ref));
		support.removeValue(ref);
		assertFalse(support.exists(ref));
	}

	/**
	 *
	 */
	@Test
	public void testQueryOnNonExistingNode() throws Exception {
		DatabaseReference ref = firebase.getReference(getTestMethodName() + "_nonExisting");
		Map<String, TestEntity> query = support.query(ref.orderByKey(), TestEntity.class);
		assertThat(query, mapWithSize(0));
	}

	/**
	 *
	 */
	@Test
	public void testQueryOnExistingNode() throws Exception {

		// prepare
		DatabaseReference ref = firebase.getReference(getTestMethodName());
		TestEntity entity1 = new TestEntity("entity1");
		TestEntity entity2 = new TestEntity("entity2");
		Map<String, TestEntity> entities = new HashMap<>();
		entities.put("key1", entity1);
		entities.put("key2", entity2);
		support.setValue(ref, entities);

		// query
		Map<String, TestEntity> query = support.query(ref.orderByKey(), TestEntity.class);

		// assert
		assertThat(query, mapWithSize(2));
		assertThat(query, hasKey("key1"));
		assertReflectionEquals(entity1, query.get("key1"));
		assertThat(query, hasKey("key2"));
		assertReflectionEquals(entity2, query.get("key2"));
	}

	/**
	 *
	 */
	@ThrowOnExtraProperties
	public static class TestEntity {

		private final String name;

		public TestEntity(String name) {
			this.name = name;
		}

		@SuppressWarnings("unused")
		private TestEntity() {
			// needed by firebase
			this(null);
		}

		public String getName() {
			return name;
		}
	}
}
