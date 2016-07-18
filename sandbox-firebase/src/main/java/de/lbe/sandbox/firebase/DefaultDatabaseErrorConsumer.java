package de.lbe.sandbox.firebase;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * @author lbeuster
 */
public class DefaultDatabaseErrorConsumer implements Consumer<DatabaseError>, BiConsumer<DatabaseError, DatabaseReference> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDatabaseErrorConsumer.class);

	@SuppressWarnings("boxing")
	@Override
	public void accept(DatabaseError error) {
		LOGGER.error("Got firebase database error {}::{}::{}", error.getCode(), error.getMessage(), error.getDetails());
	}

	@SuppressWarnings("boxing")
	@Override
	public void accept(DatabaseError error, DatabaseReference ref) {
		if (ref == null) {
			accept(error);
		} else {
			LOGGER.error("Got firebase database error on ref={}: {}::{}::{}", ref.getPath(), error.getCode(), error.getMessage(), error.getDetails());
		}
	}
}
