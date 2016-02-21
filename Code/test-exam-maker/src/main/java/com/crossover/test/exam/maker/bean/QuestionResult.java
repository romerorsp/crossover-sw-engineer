package com.crossover.test.exam.maker.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionResult {

	private boolean correct;
	private Long questionId;

	public QuestionResult(Long questionId, boolean correct) {
		this.questionId = questionId;
		this.correct = correct;
	}

	public @JsonProperty boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public @JsonProperty Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
}