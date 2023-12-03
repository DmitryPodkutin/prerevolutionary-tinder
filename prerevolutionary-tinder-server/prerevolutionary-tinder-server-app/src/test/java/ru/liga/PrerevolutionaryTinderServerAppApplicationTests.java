package ru.liga;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.ResourceBundle;

@SpringBootTest
class PrerevolutionaryTinderServerAppApplicationTests {

	@Test
	void contextLoads() {
	}

	@Bean
	public ResourceBundle logMessages() {
		return ResourceBundle.getBundle("log_message");
	}
}
