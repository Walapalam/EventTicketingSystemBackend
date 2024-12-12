package com.OOP.EventTicketingSystemBackend;

import com.OOP.EventTicketingSystemBackend.CLI.EventTicketingCLI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class EventTicketingSystemBackendApplication {

	public static void main(String[] args) {
		//SpringApplication.run(EventTicketingSystemBackendApplication.class, args);
		ApplicationContext context = SpringApplication.run(EventTicketingSystemBackendApplication.class, args);
		EventTicketingCLI cli = context.getBean(EventTicketingCLI.class);
		cli.run();
	}

}