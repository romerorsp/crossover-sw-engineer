package com.crossover.test.exam.maker.config;


import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Exam;
import com.crossover.test.exam.maker.bean.Question;
import com.crossover.test.exam.maker.bean.User;
import com.crossover.test.exam.maker.repository.ExamDAO;
import com.crossover.test.exam.maker.repository.UserDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@PropertySource("classpath:user.properties")
public class PreloadDataContextStartedListener implements ApplicationListener<ContextRefreshedEvent> {

	private @Value("classpath:/preload/user.json") Resource userResource;
	private @Value("classpath:/preload/exam.json") Resource examResource;
	private @Value("classpath:/preload/questions.json") Resource questionsResource;
	private @Value("${defaultUserPassword}") String userPassword;
	private @Autowired UserDAO userDAO;
	private @Autowired ExamDAO examDAO;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try(InputStream userIS = userResource.getInputStream();
			InputStream examIS = examResource.getInputStream();
			InputStream questionsIS = questionsResource.getInputStream()) {
			User newUser = MAPPER.readValue(userIS, User.class);
			newUser.setStringPwd(new String(Base64.getDecoder().decode(userPassword), Charset.forName("UTF-8")));
			Optional<User> storedUser = userDAO.findByLogin(newUser.getLogin()); 
			Exam newExam = MAPPER.readValue(examIS, Exam.class);
			Optional<Exam> storedExam = examDAO.findByShortName(newExam.getShortName());
			

			if(!storedExam.isPresent()) {
				newExam.setQuestions(MAPPER.readValue(questionsIS, new TypeReference<List<Question>>(){}));
			}
			
			if(storedUser.isPresent()) {
				User user = storedUser.get();
				if(user.getAvailableExam() == null) {
					user.setAvailableExam(bestChoice(newExam, storedExam));
					userDAO.save(user);
				}
			} else {
				newUser.setAvailableExam(bestChoice(newExam, storedExam));
				userDAO.save(newUser);
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).warning("Could not load the data!");
		}
	}

	private Exam bestChoice(Exam newExam, Optional<Exam> storedExam) {
		return storedExam.isPresent()? storedExam.get(): newExam;
	}
}