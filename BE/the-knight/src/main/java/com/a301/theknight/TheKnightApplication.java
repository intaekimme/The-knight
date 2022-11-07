package com.a301.theknight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableJpaAuditing
@SpringBootApplication
public class TheKnightApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheKnightApplication.class, args);
	}

}
