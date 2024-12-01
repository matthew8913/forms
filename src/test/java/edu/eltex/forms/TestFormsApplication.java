package edu.eltex.forms;

import org.springframework.boot.SpringApplication;

public class TestFormsApplication {

	public static void main(String[] args) {
		SpringApplication.from(FormsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
