/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.lbe.sandbox.spring.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class OpenWebSockets {

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	public void add(WebSocketSession session) {
		this.sessions.put(session.getId(), session);
	}

	public void doForAll(BiConsumer<String, WebSocketSession> consumer) {
		this.sessions.forEach(consumer);
	}
}
