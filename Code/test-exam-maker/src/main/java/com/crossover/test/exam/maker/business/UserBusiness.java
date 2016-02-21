package com.crossover.test.exam.maker.business;

import java.util.Collections;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.CryptoUtil;
import com.crossover.test.exam.maker.bean.User;
import com.crossover.test.exam.maker.repository.AnswerDAO;
import com.crossover.test.exam.maker.repository.UserDAO;
import com.crossover.test.exam.maker.security.UserCredentials;

@Component
public class UserBusiness {
	
	private @Autowired UserDAO userDAO;
	
	private @Value("${sessionMillis}") long sessionMillis;

	private @Autowired AnswerDAO answerDAO;

	public Optional<String> authenticate(UserCredentials credentials) {
		Optional<User> user = userDAO.findByLogin(credentials.getLogin());
		if(user.isPresent() && user.get().getPwd().equals(CryptoUtil.hash(credentials.getPwd()))) {
			return Optional.of(CryptoUtil.createJWT(Integer.toHexString(user.get().getId()), CryptoUtil.ISSUER, user.get().getName(), sessionMillis));
		}
		return Optional.empty();
	}

	public boolean validateUserJWT(String id, String name) {
		try {
			Optional<User> user = Optional.ofNullable(userDAO.findOne(Integer.parseInt(id, 32)));
			return user.isPresent() && user.get().getName().equals(name);
		} catch(Exception e) {
			return false;
		}
	}

	public Optional<User> getUser(int userId) {
		try {
			return Optional.ofNullable(userDAO.findOne(userId));
		} catch(Exception e) {
			return Optional.empty();
		}
	}

	public void save(User user) {
		userDAO.save(user);
	}

	@Transactional
	public void clearAnswers(User user) {
		answerDAO.delete(user.getAnswers());
		user.setAnswers(Collections.emptyList());
		userDAO.save(user);
	}
}