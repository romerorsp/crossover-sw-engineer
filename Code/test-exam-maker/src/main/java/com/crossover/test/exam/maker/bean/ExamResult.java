package com.crossover.test.exam.maker.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamResult {

	private List<QuestionResult> questions;
	private int corectness;

	public void setQuestions(List<QuestionResult> questions) {
		this.questions = questions;
	}

	public void setCorectness(int corectness) {
		this.corectness = corectness;		
	}

	public @JsonProperty List<QuestionResult> getQuestions() {
		return questions;
	}

	public @JsonProperty int getCorectness() {
		return corectness;
	}

}
