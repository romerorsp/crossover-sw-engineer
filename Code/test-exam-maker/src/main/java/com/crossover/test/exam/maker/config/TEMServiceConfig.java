package com.crossover.test.exam.maker.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.crossover.test.exam.maker.security.TokenBasedAuthAspect;

@Configuration
public class TEMServiceConfig extends ResourceConfig {
	
	private @Autowired TokenBasedAuthAspect aspect;
	
	public TEMServiceConfig() {
		packages("com.crossover.test.exam.maker");
		property(ServletProperties.FILTER_FORWARD_ON_404, true);
	}
}