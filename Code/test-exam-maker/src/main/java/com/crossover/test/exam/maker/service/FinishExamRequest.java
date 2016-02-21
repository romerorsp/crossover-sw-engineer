package com.crossover.test.exam.maker.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishExamRequest {
	private @JsonProperty ExamAnswer[] answers;
	public FinishExamRequest() {
		this.answers = new ExamAnswer[]{};
	}
	public FinishExamRequest(ExamAnswer[] answers) {
		this.answers = answers;
	}

	public @JsonProperty ExamAnswer[] getAnswers() {
		return answers;
	}

	public void setAnswers(ExamAnswer[] answers) {
		this.answers = answers;
	}
}
