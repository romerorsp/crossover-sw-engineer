package com.crossover.test.exam.maker.repository;

import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Answer;

@Component
public class AnswerDAO extends GenericDAO<Answer, Integer> {

	@Override
	protected Class<Answer> getBeanType() {
		return Answer.class;
	}
}