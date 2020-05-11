package de.lbe.sandbox.sse;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.lbe.sandbox.sse.SseHeartbeatRunner.HeartbeatSender;

/**
 * No auto configuration since there's no way to disable sse.
 *
 * @author lbeuster
 */
@Configuration
// @formatter:off
@Import({
	SseConnectionManager.class
})
//@formatter:on
@EnableConfigurationProperties(SseProperties.class)
public class SseConfiguration {

    @Bean
    public SseHeartbeatRunner sseHeartbeatRunner(SseConnectionManager connectionManager, HeartbeatSender heartbeatSender, SseProperties properties) {
        return new SseHeartbeatRunner(connectionManager, heartbeatSender, properties.getHeartbeatIntervalInMillis());
    }
}
