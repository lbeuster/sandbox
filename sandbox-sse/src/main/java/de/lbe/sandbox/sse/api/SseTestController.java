package de.lbe.sandbox.sse.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import de.lbe.sandbox.sse.SseConnectionManager;

/**
 * @author lbeuster
 */
@RestController
@RequestMapping(value = "/sse")
public class SseTestController {

    private final SseConnectionManager sseEmitters;

    public SseTestController(SseConnectionManager sseEmitters) {
        this.sseEmitters = sseEmitters;
    }

    @GetMapping(path = "/messages")
    @ResponseBody
    public SseEmitter messages(@RequestParam(name = "sseId") String sseId) {
        SseEmitter sseEmitter = sseEmitters.newConnection(sseId, null);
        System.out.println(sseEmitter);
        return sseEmitter;
    }

    @GetMapping(path = "/NullPointerException", produces = "text/event-stream; charset=UTF-8")
    public SseEmitter exceptionHandling() {
        throw new NullPointerException("TEST");
    }
}
