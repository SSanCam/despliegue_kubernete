package com.es.ApiLol;

import com.es.ApiLol.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ApiLolApplication extends SpringBootServletInitializer { // Extiende SpringBootServletInitializer

	public static void main(String[] args) {
		SpringApplication.run(ApiLolApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApiLolApplication.class);
	}
}
