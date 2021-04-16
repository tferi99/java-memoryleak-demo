package org.ftoth.javamemoryleakdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaMemLeakDemoApp {

	public static void main(String[] args) {
		SpringApplication.run(JavaMemLeakDemoApp.class, args);
	}
}
