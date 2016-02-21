package com.crossover.test.exam.maker.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crossover.test.exam.maker.bean.Answer;
import com.crossover.test.exam.maker.bean.ExamResult;
import com.crossover.test.exam.maker.bean.Question;
import com.crossover.test.exam.maker.bean.QuestionResult;
import com.crossover.test.exam.maker.bean.User;

public class ExamResultBuilder {

	private User user;

	private ExamResultBuilder(User user) {
		this.user = user;
	}
	
	public static ExamResultBuilder newInstance(User user) {
		return new ExamResultBuilder(user);
	}

	public ExamResult build() {
		ExamResult result = new ExamResult();
		List<Answer> answers = user.getAnswers();
		List<Question> questions = user.getAvailableExam().getQuestions();
		int questionsAmount = questions.size();
		
		List<QuestionResult> questionResults = questions.stream()
														.map(question -> {
															Optional<Boolean> optional = answers.stream()
																					 .filter(answer -> question.equals(answer.getQuestion()))
																					 .map(answer -> !answer.getOptions()
																							 			   .stream()
																							 			   .filter(option -> !option.isCorrect())
																							 			   .findAny()
																							 			   .isPresent())
																					 .findAny();
															return new QuestionResult(question.getId(), optional.isPresent() && optional.get());
																   
														})
														.collect(Collectors.toList());
		result.setQuestions(questionResults);
		result.setCorectness((((int)questionResults.stream().filter(QuestionResult::isCorrect).count())*100)/questionsAmount);
		return result;
	}

}
