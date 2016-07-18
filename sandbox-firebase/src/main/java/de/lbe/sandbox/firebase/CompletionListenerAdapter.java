package de.lbe.sandbox.firebase;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;

/**
 * @author lbeuster
 */
public class CompletionListenerAdapter implements CompletionListener {

	private Consumer<DatabaseReference> onSuccessAction;

	private BiConsumer<DatabaseError, DatabaseReference> onErrorAction = new DefaultDatabaseErrorConsumer();

	@Override
	public void onComplete(DatabaseError error, DatabaseReference ref) {
		if (error != null) {
			onError(error, ref);
		} else {
			onSuccess(ref);
		}
	}

	private void onSuccess(DatabaseReference ref) {
		Listeners.accept(onSuccessAction, ref);
	}

	private void onError(DatabaseError error, DatabaseReference ref) {
		Listeners.accept(onErrorAction, error, ref);
	}

	public CompletionListenerAdapter onSuccess(Consumer<DatabaseReference> onSuccessAction) {
		this.onSuccessAction = onSuccessAction;
		return this;
	}

	public CompletionListenerAdapter onError(BiConsumer<DatabaseError, DatabaseReference> onErrorAction) {
		this.onErrorAction = onErrorAction;
		return this;
	}
}
