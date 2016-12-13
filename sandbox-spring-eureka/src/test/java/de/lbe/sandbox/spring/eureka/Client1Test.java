package de.lbe.sandbox.spring.eureka;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

public class Client1Test {

    @Test
    public void startup() {
        Client1Main.main();
        System.out.println("started");
    }

    @SpringBootApplication
    @EnableDiscoveryClient
    public static class Client1Main {

        public static void main(String... args) {
            SpringApplication application = new SpringApplication(Client1Main.class);
            application.setAdditionalProfiles("client1");
            application.run(args);
        }
    }
}
