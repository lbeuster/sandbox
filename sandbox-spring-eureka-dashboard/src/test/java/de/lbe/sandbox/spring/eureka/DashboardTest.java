package de.lbe.sandbox.spring.eureka;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import be.ordina.msdashboard.EnableMicroservicesDashboardServer;

public class DashboardTest {

    @Test
    public void startup() {
        DashboardMain.main();
        System.out.println("started");
    }

    @SpringBootApplication
    @EnableDiscoveryClient
    @EnableMicroservicesDashboardServer
    public static class DashboardMain {

        public static void main(String... args) {
            SpringApplication application = new SpringApplication(DashboardMain.class);
            application.setAdditionalProfiles("dashboard");
            application.run(args);
        }
    }
}
