package com.crossover.test.exam.maker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Online Test Exam Maker - Service.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages="com.crossover.test.exam.maker")
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		new Application().configure(new SpringApplicationBuilder(Application.class)).run(args);
	}
}