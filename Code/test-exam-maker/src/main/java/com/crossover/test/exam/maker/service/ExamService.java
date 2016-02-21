package com.crossover.test.exam.maker.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Answer;
import com.crossover.test.exam.maker.bean.Exam;
import com.crossover.test.exam.maker.bean.ExamStatus;
import com.crossover.test.exam.maker.bean.Option;
import com.crossover.test.exam.maker.bean.Question;
import com.crossover.test.exam.maker.bean.User;
import com.crossover.test.exam.maker.bean.UserSession;
import com.crossover.test.exam.maker.business.AnswerBusiness;
import com.crossover.test.exam.maker.business.ExamBusiness;
import com.crossover.test.exam.maker.business.OptionBusiness;
import com.crossover.test.exam.maker.business.QuestionBusiness;
import com.crossover.test.exam.maker.business.UserBusiness;
import com.crossover.test.exam.maker.business.UserSessionBusiness;
import com.crossover.test.exam.maker.security.Authenticated;
import com.crossover.test.exam.maker.util.ExamResultBuilder;

@Component
@Path(ExamService.PATH)
public class ExamService {

	private static final String EXAM_IN_PROGRESS = "examInProgress";
	public static final String PATH = "/exam";
	private @Autowired ExamBusiness examBusiness;
	private @Autowired QuestionBusiness questionBusiness;
	private @Autowired OptionBusiness optionBusiness;
	private @Autowired UserBusiness userBusiness;
	private @Autowired AnswerBusiness answerBusiness;
	private @Autowired UserSessionBusiness userSessionBusiness;
	
	@GET
	@Authenticated
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response retrieve(@NotNull @PathParam("userId") int userId) {
		Optional<JSONObject> json = examBusiness.getExamForUserAsJSON(userId);
		if(json.isPresent()) {
			return Response.ok().entity(json.get().toString()).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@PUT
	@Authenticated
	@Path("/start/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(rollbackOn=Throwable.class)
	public Response startExam(@NotNull @PathParam("userId") int userId, @Context HttpServletRequest request) {
		HttpSession session = request.getSession();
		Optional<User> user = userBusiness.getUser(userId);
		Exam exam;
		if(!(user.isPresent() && (exam = user.get().getAvailableExam()) != null)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Optional<UserSession> optionalSession = userSessionBusiness.getSession(user.get());
		UserSession uSession;
		if(!optionalSession.isPresent()) {
			uSession = new UserSession();
			uSession.setDuration(exam.getDuration());
			uSession.setExamStatus(ExamStatus.INPROGRESS);
			uSession.setStarting(new Date());
			uSession.setUser(user.get());
			userSessionBusiness.saveSession(uSession);
		} else {
			uSession = optionalSession.get();
			if(uSession.getExamStatus() == ExamStatus.FINISHED) {
				// It means the user is restarting the test, so let's clear the data...
				uSession.setDuration(exam.getDuration());
				uSession.setExamStatus(ExamStatus.INPROGRESS);
				uSession.setStarting(new Date());
				userBusiness.clearAnswers(user.get());
			}
		}
		if(uSession.getExamStatus() != null && uSession.getExamStatus() != ExamStatus.INPROGRESS) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		session.setMaxInactiveInterval(Long.valueOf(uSession.getRemaining()/1000).intValue());
		session.setAttribute(EXAM_IN_PROGRESS, uSession);
		
		return Response.ok().build();
	}
	
	@PUT
	@Authenticated
	@Transactional
	@Path("/finish/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finishExam(@NotNull @PathParam("userId") int userId, List<ExamAnswer> answers, @Context HttpServletRequest request) {
		HttpSession session = request.getSession();
		Optional<User> user = userBusiness.getUser(userId);
		if(!(user.isPresent() && user.get().getAvailableExam() != null)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Optional<Object> sessionAttribute = Optional.ofNullable(session.getAttribute(EXAM_IN_PROGRESS));
		Optional<UserSession> uSession;
		if(sessionAttribute.isPresent()) {
			uSession = Optional.of(UserSession.class.cast(sessionAttribute.get()));
		} else {
			uSession = userSessionBusiness.getSession(user.get());
		}
		if(!uSession.isPresent()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<Long> possibleQuestions = user.get().getAvailableExam().getQuestions().stream().map(Question::getId).collect(Collectors.toList());
		List<Answer> list = answers.stream()
								   .filter(examAnswer -> possibleQuestions.contains(examAnswer.getQuestionId()))
								   .map(examAnswer -> {
									   List<Option> options = examAnswer.getOptions()
											  						    .stream()
											  						    .map(id -> optionBusiness.getOption(id))
											  						    .filter(Optional::isPresent)
											  						    .map(Optional::get)
											  						    .filter(option -> option.getQuestion().getId().equals(examAnswer.getQuestionId()))
											  						    .collect(Collectors.toList());
									   Answer answer = new Answer();
									   answer.setOptions(options);
									   return answer;
								   })
								   .collect(Collectors.toList());
		user.get().setAnswers(list);
		userBusiness.save(user.get());
		uSession.get().setExamStatus(ExamStatus.FINISHED);
		session.removeAttribute(EXAM_IN_PROGRESS);
		userSessionBusiness.saveSession(uSession.get());
		return Response.ok(ExamResultBuilder.newInstance(user.get()).build()).build();
	}
	
	@GET
	@Authenticated
	@Path("/status/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getStatus(@NotNull @PathParam("userId") int userId, @Context HttpServletRequest request) {
		HttpSession session = request.getSession();
		Optional<Object> sessionAttribute = Optional.ofNullable(session.getAttribute(EXAM_IN_PROGRESS));
		Optional<UserSession> uSession;
		if(sessionAttribute.isPresent()) {
			uSession = Optional.of(UserSession.class.cast(sessionAttribute.get()));
		} else {
			Optional<User> user = userBusiness.getUser(userId);
			if(!(user.isPresent() && user.get().getAvailableExam() != null)) {
				return Response.status(Status.NOT_FOUND).build();
			}
			uSession = userSessionBusiness.getSession(user.get());
		}
		if(!uSession.isPresent()) {
			return Response.status(Status.NOT_FOUND).build();
		}

		if(uSession.get().getRemaining() < 1) {
			userSessionBusiness.setTimedout(uSession.get());
			session.setMaxInactiveInterval(0);
		}
		
		return Response.ok(uSession.get()).build();
	}
	
	@PUT
	@Authenticated
	@Path("/clear/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response clear(@NotNull @PathParam("userId") int userId, @Context HttpServletRequest request) {
		HttpSession session = request.getSession();
		Optional<Object> sessionAttribute = Optional.ofNullable(session.getAttribute(EXAM_IN_PROGRESS));
		if(sessionAttribute.isPresent()) {
			session.removeAttribute(EXAM_IN_PROGRESS);
		} else {
			Optional<User> user = userBusiness.getUser(userId);
			if(!(user.isPresent() && user.get().getAvailableExam() != null)) {
				return Response.status(Status.NOT_FOUND).build();
			}
			userSessionBusiness.clearSession(user.get());
		}

		return Response.ok().build();
	}
}