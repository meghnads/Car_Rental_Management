package com.trimble.car.lease.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration.class})
class TrimbleCarsLeaseApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrimbleCarsLeaseApplication.class, args);
	}
}
