package com.valanse.valanse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.valanse.valanse.repository.jpa")
@EnableRedisRepositories(basePackages = "com.valanse.valanse.redis")
public class ValanseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValanseApplication.class, args);
	}
}
