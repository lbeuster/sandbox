package de.lbe.sandbox.mongodb.official;

import javax.net.SocketFactory;

import com.mongodb.MongoClientOptions.Builder;

/**
 * @author lars.beuster
 */
public class MongoClientOptionsBuilder extends Builder {

	/**
	 * The original mongo-driver (2.11.4, haven't tried higer versions) always initializes the socket factory with it's own value if ssl=true is given in gthe URI. We have to
	 * ignore this call.
	 */
	private boolean freezeSocketFactory = false;

	public MongoClientOptionsBuilder freezeSocketFactory() {
		this.freezeSocketFactory = true;
		return this;
	}

	@Override
	public MongoClientOptionsBuilder socketFactory(SocketFactory socketFactory) {
		if (!this.freezeSocketFactory) {
			super.socketFactory(socketFactory);
		}
		return this;
	}
}
