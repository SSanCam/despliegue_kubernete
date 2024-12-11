package com.es.ApiLol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties()
public class ApiLolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiLolApplication.class, args);
	}

}
