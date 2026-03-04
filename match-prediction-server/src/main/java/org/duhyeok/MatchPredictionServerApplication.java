package org.duhyeok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@SpringBootApplication
public class MatchPredictionServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchPredictionServerApplication.class, args);
	}

}
