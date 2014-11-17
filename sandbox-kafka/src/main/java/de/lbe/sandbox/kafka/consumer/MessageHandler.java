package de.lbe.sandbox.kafka.consumer;

import kafka.message.MessageAndMetadata;

/**
 * @author lbeuster
 */
public interface MessageHandler {

	void handle(MessageAndMetadata<byte[], byte[]> message);
}