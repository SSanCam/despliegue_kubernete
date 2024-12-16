package com.es.ApiLol;

import com.es.ApiLol.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ApiLolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiLolApplication.class, args);
	}

}
