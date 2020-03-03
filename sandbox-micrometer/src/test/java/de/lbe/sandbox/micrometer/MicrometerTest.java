package de.lbe.sandbox.micrometer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/**
 * @author lbeuster
 */
public class MicrometerTest extends AbstractJUnitTest {

    private MeterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
    }

    @Test
    public void test_manual_gauge() {
        AtomicInteger gauge = registry.gauge("test.gauge", new AtomicInteger(0));
        gauge.set(4);
    }

    @Test
    void test_automatic_gauge() {
        class TestObject {
            long currentTime() {
                return System.currentTimeMillis();
            }
        }
        TestObject gauge = registry.gauge(getTestMethodName(), new TestObject(), obj -> obj.currentTime());
    }

    @Test
    void test_counter() {
        Counter counter = registry.counter(getTestMethodName());
        counter.increment();
    }

    @Test
    public void test_timer() {
        Timer timer = registry.timer("test.timer");
        timer.record(Duration.ofMillis(10));
    }

    @Test
    public void test_method_timer() {
        Timer timer = registry.timer("test.timer", "key", "value");
        Supplier<String> methodWithReturnValue = () -> getTestMethodName();
        String name = timer.record(methodWithReturnValue);
        assertEquals(getTestMethodName(), name);
    }
}