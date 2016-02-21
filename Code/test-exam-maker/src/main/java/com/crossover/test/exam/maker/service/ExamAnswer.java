package com.crossover.test.exam.maker.service;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamAnswer {

	private @JsonProperty Long questionId;
	private @JsonProperty List<Long> options = Collections.emptyList();


	public List<Long> getOptions() {
		return options;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public void setOptions(List<Long> options) {
		this.options = options;
	}
}