package com.crossover.test.exam.maker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Exam;
import com.crossover.test.exam.maker.bean.User;

@Component
public class UserDAO extends GenericDAO<User, Integer> {

	@Override
	protected Class<User> getBeanType() {
		return User.class;
	}

	public Optional<Exam> getAvailableExam(int userId) {
		User user = this.findOne(userId);
		return user != null? Optional.ofNullable(user.getAvailableExam()): Optional.empty();
	}

	public Optional<User> findByLogin(String login) {
		List<User> result = em.createQuery("FROM User u WHERE u.login = :login", getBeanType()).setParameter("login", login).getResultList();
		return result.isEmpty()? Optional.empty(): Optional.of(result.get(0));
	}
}