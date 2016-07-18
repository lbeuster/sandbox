package de.lbe.sandbox.spring.data;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Verify;

import de.asideas.ipool.commons.lib.lang.ToString;

@Document(collection = "users")
public class User {

	private final String myId;

	private final String username;

	private final String password;

	private final ZonedDateTime zonedDateTime;

	public User(String myId, String username, String password, ZonedDateTime zonedDateTime) {
		Verify.verifyNotNull(myId, "id");
		this.myId = myId;
		this.username = username;
		this.password = password;
		this.zonedDateTime = zonedDateTime;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Id
	public String getMyId() {
		return myId;
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}

	public ZonedDateTime getZonedDateTime() {
		return zonedDateTime;
	}
}