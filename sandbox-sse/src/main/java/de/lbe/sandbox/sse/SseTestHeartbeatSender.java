package de.lbe.sandbox.sse;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import de.lbe.sandbox.sse.SseHeartbeatRunner.HeartbeatSender;

/**
 * @author lbeuster
 */
@Component
public class SseTestHeartbeatSender implements HeartbeatSender {

    @Override
    public void send(SseEmitter emitter) throws IOException {
        emitter.send(new SseMessage("heartbeat"));
    }
}
