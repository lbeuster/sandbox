package de.lbe.sandbox.firebase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseOptions.Builder;

import de.asideas.ipool.commons.lib.test.junit.AbstractJUnitTest;
import de.asideas.ipool.commons.lib.test.junit.rules.TestFilesRule;
import de.lbe.sandbox.firebase.FirebaseClient;

/**
 * @author lbeuster
 */
public abstract class AbstractFirebaseIT extends AbstractJUnitTest {

	@Rule
	public TestFilesRule testFiles = new TestFilesRule();

	protected FirebaseClient firebase;

	/**
	 *
	 */
	@Before
	public void setUpFirebase() throws Exception {

		// let's see if we already have an app
		FirebaseApp app;
		try {
			app = FirebaseApp.getInstance();
		} catch (@SuppressWarnings("unused") IllegalStateException ex) {
			app = null;
		}

		// create the client
		if (app != null) {
			firebase = new FirebaseClient(app);
		} else {
			FirebaseOptions options = newFirebaseOptions();
			firebase = new FirebaseClient(options);
		}

		// ensure that everything is initialized (the first read/write may take a little bit longer)
		firebase.awaitFirstCompletion();
	}

	/**
	 *
	 */
	@After
	public void tearDownFirebase() throws Exception {
		// wait for outstanding operations (e.g. a delete)
		firebase.awaitCompletion();
	}

	/**
	 *
	 */
	protected FirebaseOptions newFirebaseOptions() throws IOException, FileNotFoundException {
		InputStream credentials = testFiles.resolveUrl("/auth.json").openStream();
		Builder builder = new FirebaseOptions.Builder().setServiceAccount(credentials).setDatabaseUrl("https://ipool-it.firebaseio.com");
		FirebaseOptions options = builder.build();
		credentials.close();
		return options;
	}
}
