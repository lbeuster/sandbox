package de.lbe.sandbox.sse.client;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;

import de.lbe.sandbox.sse.SseConnectionManager;
import de.lbe.sandbox.sse.SseMain;

/**
 * @author lbeuster
 */
@SpringBootTest(classes = AbstractSseIT.TestConfiguration.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AbstractSseIT extends AbstractJUnitTest {

    @Autowired
    private SseConnectionManager manager;

    private Client client;

    private final List<SseTarget> sseTargets = new ArrayList<>();

    @Autowired
    private ServerProperties serverProperties;

    @LocalServerPort
    private int httpPort;

    /**
     *
     */
    @BeforeEach
    public void setUp() {
        this.client = new SseClientBuilder().builder().build();
    }

    /**
     *
     */
    @AfterEach
    public void tearDown() throws Exception {
        sseTargets.forEach(target -> target.close());
        this.client.close();
        manager.closeConnections();
    }

    @SuppressWarnings("boxing")
    protected String baseUrl() {
        return String.format("http://localhost:%s", httpPort);
    }

    protected WebTarget webTarget() {
        return client.target(baseUrl());
    }

    protected SseTarget defaultSseTarget(String sseId) {
        WebTarget webTarget = webTarget().path("/sse/messages").queryParam("sseId", sseId);
        return sseTarget(webTarget);
    }

    protected SseTarget sseTarget(WebTarget webTarget) {
        SseTarget sseTarget = new SseTarget(webTarget);
        this.sseTargets.add(sseTarget);
        return sseTarget;
    }

    /**
     *
     */
    @Configuration
    @Import({ SseMain.class })
    static class TestConfiguration {
        // only the annotations
    }
}
