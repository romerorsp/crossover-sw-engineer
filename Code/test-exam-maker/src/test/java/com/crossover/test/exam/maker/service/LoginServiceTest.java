package com.crossover.test.exam.maker.service;

import java.net.URI;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.crossover.test.exam.maker.Application;
import com.crossover.test.exam.maker.service.test.BaseRestTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest(randomPort = true)
public class LoginServiceTest extends BaseRestTest {
	
    public @Test void rightUserPwdTest() throws Exception {
    	URI targetUri = UriComponentsBuilder.fromUri(uri).path(LoginService.PATH).build().toUri();

    	Response response = RestAssured.given()
    								   .contentType(ContentType.JSON)
    								   .accept(ContentType.TEXT)
    								   .header("l", "staff@crossover.com")
    								   .header("p", "crossover")
    								   .post(targetUri);

    	Assert.assertEquals("expected status code 200", Status.OK.getStatusCode(), response.getStatusCode());

        String token = response.getBody().asString();
        Assert.assertNotNull("Security token must not be null", token);
        Assert.assertFalse("Security token must not be empty", token.isEmpty());
    }

    @Test
    public void wrongUserPwdTest() throws Exception {
    	URI targetUri = UriComponentsBuilder.fromUri(uri).path(LoginService.PATH).build().toUri();

    	Response response = RestAssured.given()
    								   .contentType(ContentType.JSON)
				   					   .header("l", "staff@crossover.com")
    								   .header("p", "asd")
    								   .post(targetUri);
    	Assert.assertEquals("expected status code FORBIDDEN", Status.FORBIDDEN.getStatusCode(), response.getStatusCode());
    }

	@Override
	protected boolean isSecure() {
		return false;
	}
}
