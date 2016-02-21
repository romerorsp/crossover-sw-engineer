package com.crossover.test.exam.maker.service.test;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.crossover.test.exam.maker.Application;
import com.crossover.test.exam.maker.bean.Exam;
import com.crossover.test.exam.maker.bean.ExamResult;
import com.crossover.test.exam.maker.bean.Question;
import com.crossover.test.exam.maker.service.ExamAnswer;
import com.crossover.test.exam.maker.service.ExamService;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.jayway.restassured.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest(randomPort = true)
public class ExamServiceTest extends BaseRestTest {
	
	public @Test void retrieveExamTest() {
		URI targetUri = UriComponentsBuilder.fromUri(uri)
											.path(ExamService.PATH)
											.path("/1") //user id (preloaded)
											.build()
											.toUri();
		Response result = RestAssured.given()
									 .header(SECURITY_TOKEN, this.token)
									 .contentType(MediaType.APPLICATION_JSON)
  								   	 .accept(MediaType.APPLICATION_JSON)
									 .get(targetUri);
		Assert.assertEquals("The status code should be OK!", Status.OK.getStatusCode(), result.getStatusCode());
		Exam exam = result.as(Exam.class, ObjectMapperType.GSON);
		Assert.assertEquals("Exam should be FIRSTEXAM", "FIRSTEXAM", exam.getShortName());
		Assert.assertTrue("Questions List should be size 1", exam.getQuestions().size() == 10);
		Question question = exam.getQuestions().get(0);
		Assert.assertEquals("Question should be FIRSTEXAM", "FIRSTQ", question.getShortName());
		Assert.assertTrue("Options List should be size 4", question.getOptions().size() == 4);
		Assert.assertEquals("Exam should be FIRSTEXAM", "FIRSTO", question.getOptions().get(0).getShortName());
		Assert.assertEquals("Exam should be FIRSTEXAM", "SECONO", question.getOptions().get(1).getShortName());
		Assert.assertEquals("Exam should be FIRSTEXAM", "THIRDO", question.getOptions().get(2).getShortName());
		Assert.assertEquals("Exam should be FIRSTEXAM", "FOURO", question.getOptions().get(3).getShortName());
	}
	
	public @Test void retrieveExamNoAuthTest() {
		URI targetUri = UriComponentsBuilder.fromUri(uri)
											.path(ExamService.PATH)
											.path("/1") //user id (preloaded)
											.build()
											.toUri();
		Response result = RestAssured.given()
									 .contentType(MediaType.APPLICATION_JSON)
									 .get(targetUri);
		Assert.assertEquals("The status code should be FORBIDDEN!", Status.FORBIDDEN.getStatusCode(), result.getStatusCode());
	}
	
	public @Test void startAndFinishTest() {

		URI targetUri = UriComponentsBuilder.fromUri(uri)
											.path(ExamService.PATH)
											.path("/clear/1") //user id (preloaded)
											.build()
											.toUri();
		Response result = RestAssured.given()
									 .header(SECURITY_TOKEN, this.token)
									 .contentType(MediaType.APPLICATION_JSON)
  								   	 .accept(MediaType.APPLICATION_JSON)
									 .put(targetUri);
		Assert.assertEquals("The status code should be OK!", Status.OK.getStatusCode(), result.getStatusCode());
		
		targetUri = UriComponentsBuilder.fromUri(uri)
											.path(ExamService.PATH)
											.path("/start/1") //user id (preloaded)
											.build()
											.toUri();
		result = RestAssured.given()
							.header(SECURITY_TOKEN, this.token)
							.contentType(MediaType.APPLICATION_JSON)
						   	.accept(MediaType.APPLICATION_JSON)
						   	.put(targetUri);
		Assert.assertEquals("The status code should be OK!", Status.OK.getStatusCode(), result.getStatusCode());
		
		targetUri = UriComponentsBuilder.fromUri(uri)
										.path(ExamService.PATH)
										.path("/finish/1") //user id (preloaded)
										.build()
										.toUri();
		List<ExamAnswer> answers = buildAnswers();
		result = RestAssured.given()
							.header(SECURITY_TOKEN, this.token)
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.APPLICATION_JSON)
							.content(answers)
							.put(targetUri);
		ExamResult examResult = result.as(ExamResult.class, ObjectMapperType.GSON);
		Assert.assertEquals("User should be 80% correct", 80, examResult.getCorectness());
	}
	
	private List<ExamAnswer> buildAnswers() {
		ExamAnswer[] answers = new ExamAnswer[10];
		for(int i = 0; i < 10; i++) {
			answers[i] = new ExamAnswer();
			answers[i].setQuestionId(i + 1L);
		}
		answers[0].setOptions(Collections.singletonList(2L));
		answers[1].setOptions(Collections.singletonList(5L));
		answers[2].setOptions(Arrays.asList(new Long[]{7L, 8L}));
		answers[3].setOptions(Collections.singletonList(10L));
		answers[4].setOptions(Collections.singletonList(11L));
		answers[5].setOptions(Collections.singletonList(13L));
		answers[6].setOptions(Collections.singletonList(15L));
		answers[7].setOptions(Collections.singletonList(17L));
		//Ill pretend I don't know the answer below...
		answers[8].setOptions(Collections.singletonList(20L));//19L is the right, instead...
		//I'll pretend I did not answer the question below...
		//answers[9].setOptions(Collections.singletonList(21L));
		return Arrays.asList(answers);
	}

	public @Test void statusCheckTest() {

		URI targetUri = UriComponentsBuilder.fromUri(uri)
											.path(ExamService.PATH)
											.path("/clear/1") //user id (preloaded)
											.build()
											.toUri();
		Response result = RestAssured.given()
									 .header(SECURITY_TOKEN, this.token)
									 .contentType(MediaType.APPLICATION_JSON)
  								   	 .accept(MediaType.APPLICATION_JSON)
									 .put(targetUri);
		Assert.assertEquals("The status code should be OK!", Status.OK.getStatusCode(), result.getStatusCode());
		
		targetUri = UriComponentsBuilder.fromUri(uri)
											.path(ExamService.PATH)
											.path("/start/1") //user id (preloaded)
											.build()
											.toUri();
		result = RestAssured.given()
							.header(SECURITY_TOKEN, this.token)
							.contentType(MediaType.APPLICATION_JSON)
						   	.accept(MediaType.APPLICATION_JSON)
						   	.put(targetUri);
		Assert.assertEquals("The status code should be OK!", Status.OK.getStatusCode(), result.getStatusCode());
		int sleep = 1000;
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {}
		targetUri = UriComponentsBuilder.fromUri(uri)
										.path(ExamService.PATH)
										.path("/status/1") //user id (preloaded)
										.build()
										.toUri();
		result = RestAssured.given()
							.header(SECURITY_TOKEN, this.token)
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.APPLICATION_JSON)
							.get(targetUri);
		Assert.assertEquals("The status code should be OK!", Status.OK.getStatusCode(), result.getStatusCode());
		JSONObject json = new JSONObject(result.getBody().asString());
		Assert.assertTrue("Remaning should < %d.", json.getLong("remaining") <= (System.currentTimeMillis() - json.getLong("duration") - sleep));
	}
	
	@Override
	protected boolean isSecure() {
		return true;
	}
}