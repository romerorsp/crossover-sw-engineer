package com.crossover.test.exam.maker.service;

import java.util.Optional;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.business.UserBusiness;
import com.crossover.test.exam.maker.security.UserCredentials;

@Component
@Path(LoginService.PATH)
public class LoginService {

	public static final String PATH = "/auth";
	public static final String SECURITY_TOKEN = "jwt-security-token";
	private @Autowired UserBusiness business;
	
	@POST
	@Consumes(value=MediaType.APPLICATION_JSON)
	@Produces(value=MediaType.TEXT_PLAIN)
	public Response authenticate(@BeanParam UserCredentials credentials) {
		Optional<String> token = business.authenticate(credentials);
		if(token.isPresent()) {
			return Response.ok(token.get()).build();
		}
		return Response.status(Status.FORBIDDEN).build();
	}
}