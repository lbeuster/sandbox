package de.lbe.sandbox.spring.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class HystrixMain {

	public static void main(String... args) {
		SpringApplication.run(HystrixMain.class, args);
	}
}
