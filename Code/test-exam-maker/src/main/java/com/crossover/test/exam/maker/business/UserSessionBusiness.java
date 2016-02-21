package com.crossover.test.exam.maker.business;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.ExamStatus;
import com.crossover.test.exam.maker.bean.User;
import com.crossover.test.exam.maker.bean.UserSession;
import com.crossover.test.exam.maker.repository.UserSessionDAO;

@Component
public class UserSessionBusiness {
	
	private @Autowired UserSessionDAO userSessionDAO;

	@Transactional
	public void saveSession(UserSession uSession) {
		userSessionDAO.merge(uSession);
	}

	@Transactional
	public Optional<UserSession> getSession(User user) {
		Optional<UserSession> uSession = userSessionDAO.getSession(user);
		uSession.ifPresent(session -> {
			if(session.getExamStatus() == ExamStatus.INPROGRESS && (session.getDuration() + session.getStarting().getTime()) <= System.currentTimeMillis()) {
				this.setTimedout(session);
			}
		});
		return uSession;
	}

	@Transactional
	public void setTimedout(UserSession session) {
		session.setExamStatus(ExamStatus.TIMEDOUT);
		userSessionDAO.save(session);
	}

	@Transactional
	public void clearSession(User user) {
		Optional<UserSession> session = getSession(user);
		if(session.isPresent()) {
			userSessionDAO.delete(session.get());
		}
	}	
}