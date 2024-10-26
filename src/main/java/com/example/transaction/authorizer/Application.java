package com.example.transaction.authorizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableJpaRepositories("com.example.transaction.authorizer.repository")
@EntityScan(basePackages = "com.example.transaction.authorizer.model")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
