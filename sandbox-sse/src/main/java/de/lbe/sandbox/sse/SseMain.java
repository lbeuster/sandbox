package de.lbe.sandbox.sse;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cartelsol.commons.lib.spring.boot.ExtendedSpringApplicationBuilder;

/**
 * @author lbeuster
 */
@SpringBootApplication
public class SseMain {

    public static void main(final String[] args) {
        new ExtendedSpringApplicationBuilder(SseMain.class).keepMainThreadAlive(true).run(args);
    }
}
