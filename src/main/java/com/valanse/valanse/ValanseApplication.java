package com.valanse.valanse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.valanse.valanse.redis")
public class ValanseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValanseApplication.class, args);
	}

}
