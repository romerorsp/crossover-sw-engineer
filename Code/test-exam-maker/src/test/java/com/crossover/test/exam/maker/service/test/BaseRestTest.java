package com.crossover.test.exam.maker.service.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.crossover.test.exam.maker.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public abstract class BaseRestTest {
	protected final static String SECURITY_TOKEN = LoginService.SECURITY_TOKEN;

	@Value("${local.server.port}")
	protected int port;

	protected URI uri;

	protected RestTemplate rest;

	protected String token;
	
	protected static final ObjectMapper MAPPER = new ObjectMapper();

	public @Before void setup() {
		rest = new TestRestTemplate();
		try {
			uri = new URI(String.format("http://localhost:%d/tem", port));
		} catch (URISyntaxException e) {
			Assert.fail("Your URI should be in a good syntax.");
		}

		if(isSecure()) {
	    	URI targetUri = UriComponentsBuilder.fromUri(uri).path(LoginService.PATH).build().toUri();

	    	Response response = RestAssured.given()
	    								   .contentType(ContentType.JSON)
	    								   .accept(ContentType.TEXT)
	    								   .header("l", "staff@crossover.com")
	    								   .header("p", "crossover")
	    								   .post(targetUri);
	        this.token = response.getBody().asString();
		}
	}

	protected abstract boolean isSecure();
}
