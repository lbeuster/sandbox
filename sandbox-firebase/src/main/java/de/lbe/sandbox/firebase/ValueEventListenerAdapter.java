package de.lbe.sandbox.firebase;

import java.util.function.Consumer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * @author lbeuster
 */
public class ValueEventListenerAdapter implements ValueEventListener {

	private Consumer<DataSnapshot> onDataChangeAction;

	private Consumer<DatabaseError> onCancelledAction = new DefaultDatabaseErrorConsumer();

	@Override
	public void onDataChange(DataSnapshot data) {
		if (this.onDataChangeAction != null) {
			this.onDataChangeAction.accept(data);
		}
	}

	@Override
	public void onCancelled(DatabaseError error) {
		if (this.onCancelledAction != null) {
			this.onCancelledAction.accept(error);
		}
	}

	public ValueEventListenerAdapter onDataChange(Consumer<DataSnapshot> onDataChangeAction) {
		this.onDataChangeAction = new ThrowableLoggingConsumer<>(onDataChangeAction);
		return this;
	}

	public ValueEventListenerAdapter onCancelled(Consumer<DatabaseError> onCancelledAction) {
		this.onCancelledAction = onCancelledAction;
		return this;
	}
}
