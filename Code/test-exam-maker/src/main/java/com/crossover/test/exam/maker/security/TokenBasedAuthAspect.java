package com.crossover.test.exam.maker.security;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.CryptoUtil;
import com.crossover.test.exam.maker.business.UserBusiness;
import com.crossover.test.exam.maker.service.LoginService;

@Aspect
@Component
public class TokenBasedAuthAspect implements ApplicationEventListener, RequestEventListener {

	private Optional<String> token;
	private @Autowired ResourceConfig config;
	private @Autowired UserBusiness userBusiness;
	
	public @PostConstruct void setup() {
		config.register(this);
	}

	@Around(value="@within(com.crossover.test.exam.maker.security.Authenticated) || @annotation(com.crossover.test.exam.maker.security.Authenticated)")
	public Object checkToken(ProceedingJoinPoint jp) throws Throwable {
		if(!(token.isPresent() && CryptoUtil.validateJWT(token.get(), userBusiness))) {
			return Response.status(Status.FORBIDDEN).build();
		}
		return jp.proceed();
	}

	@Override
	public void onEvent(ApplicationEvent event) {}

	@Override
	public RequestEventListener onRequest(RequestEvent event) {
		return this;
	}

	@Override
	public void onEvent(RequestEvent event) {
		this.token = Optional.ofNullable(event.getContainerRequest().getHeaderString(LoginService.SECURITY_TOKEN));
	}	
}
