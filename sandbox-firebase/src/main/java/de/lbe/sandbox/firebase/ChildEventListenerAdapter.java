package de.lbe.sandbox.firebase;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * @author lbeuster
 */
public class ChildEventListenerAdapter implements ChildEventListener {

	private BiConsumer<DataSnapshot, String> onChildAddedAction;

	private BiConsumer<DataSnapshot, String> onChildChangedAction;

	private Consumer<DataSnapshot> onChildRemovedAction;

	private BiConsumer<DataSnapshot, String> onChildMovedAction;

	private Consumer<DatabaseError> onCancelledAction = new DefaultDatabaseErrorConsumer();

	@Override
	public void onChildAdded(DataSnapshot data, String param) {
		Listeners.accept(onChildAddedAction, data, param);
	}

	@Override
	public void onChildChanged(DataSnapshot data, String param) {
		Listeners.accept(onChildChangedAction, data, param);
	}

	@Override
	public void onChildRemoved(DataSnapshot data) {
		Listeners.accept(onChildRemovedAction, data);
	}

	@Override
	public void onChildMoved(DataSnapshot data, String param) {
		Listeners.accept(onChildMovedAction, data, param);
	}

	@Override
	public void onCancelled(DatabaseError error) {
		Listeners.accept(onCancelledAction, error);
	}

	public ChildEventListenerAdapter onChildAdded(BiConsumer<DataSnapshot, String> onChildAddedAction) {
		this.onChildAddedAction = onChildAddedAction;
		return this;
	}

	public ChildEventListenerAdapter onChildChanged(BiConsumer<DataSnapshot, String> onChildChangedAction) {
		this.onChildChangedAction = onChildChangedAction;
		return this;
	}

	public ChildEventListenerAdapter onChildRemoved(Consumer<DataSnapshot> onChildRemovedAction) {
		this.onChildRemovedAction = onChildRemovedAction;
		return this;
	}

	public ChildEventListenerAdapter onChildMoved(BiConsumer<DataSnapshot, String> onChildMovedAction) {
		this.onChildMovedAction = onChildMovedAction;
		return this;
	}

	public ChildEventListenerAdapter onCancelled(Consumer<DatabaseError> onCancelledAction) {
		this.onCancelledAction = onCancelledAction;
		return this;
	}
}
