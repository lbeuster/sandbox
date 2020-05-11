package de.lbe.sandbox.sse;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.cartelsol.commons.lib.lang.ToString;

/**
 * @author lbeuster
 */
@ConfigurationProperties(prefix = "sse", ignoreUnknownFields = false)
public class SseProperties {

    /**
     * Since Heroku cancels the connection after 55 secs we should have a value below this timeout here.
     */
    private long heartbeatIntervalInMillis = 20_000;

    public long getHeartbeatIntervalInMillis() {
        return heartbeatIntervalInMillis;
    }

    public void setHeartbeatIntervalInMillis(long heartbeatInterval) {
        this.heartbeatIntervalInMillis = heartbeatInterval;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
