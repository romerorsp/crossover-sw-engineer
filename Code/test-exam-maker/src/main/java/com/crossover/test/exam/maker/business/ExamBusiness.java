package com.crossover.test.exam.maker.business;

import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Exam;
import com.crossover.test.exam.maker.repository.ExamDAO;
import com.crossover.test.exam.maker.repository.UserDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ExamBusiness {

	private @Autowired ExamDAO examDAO;
	private @Autowired UserDAO userDAO;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public Optional<Exam> getExamForUser(int userId) {
		return userDAO.getAvailableExam(userId);
	}

	/*
	 * Avoiding Problems with LazyInitException.
	 */
	public Optional<JSONObject> getExamForUserAsJSON(int userId) {
		try {
			Optional<Exam> exam = this.getExamForUser(userId);
			return exam.isPresent()? Optional.of(new JSONObject(MAPPER.writeValueAsString(exam.get()))): Optional.empty();
		} catch (JSONException | JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}