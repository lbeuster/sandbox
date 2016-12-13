package de.lbe.sandbox.spring.eureka;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

public class EurekaServerTest {

    @Test
    public void startup() {
        EurekaServerMain.main();
        System.out.println("started");
    }

    @SpringBootApplication
    @EnableEurekaServer
    @EnableDiscoveryClient
    public static class EurekaServerMain {

        public static void main(String... args) {
            SpringApplication application = new SpringApplication(EurekaServerMain.class);
            application.setAdditionalProfiles("eureka-server");
            application.run(args);
        }
    }
}
