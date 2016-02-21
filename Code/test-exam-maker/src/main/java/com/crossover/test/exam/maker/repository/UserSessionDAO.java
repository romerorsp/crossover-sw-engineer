package com.crossover.test.exam.maker.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.User;
import com.crossover.test.exam.maker.bean.UserSession;

@Component
public class UserSessionDAO extends GenericDAO<UserSession, Integer> {

	@Override
	protected Class<UserSession> getBeanType() {
		return UserSession.class;
	}

	public Optional<UserSession> getSession(@NotNull User user) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserSession> criteriaQuery = criteriaBuilder.createQuery(getBeanType());

        Root<UserSession> root = criteriaQuery.from(getBeanType());
        criteriaQuery.select(root);
        
        Predicate userPredicate = criteriaBuilder.equal(root.get("user"), user);
        criteriaQuery = criteriaQuery.where(userPredicate);
        
        TypedQuery<UserSession> query = em.createQuery(criteriaQuery);
        List<UserSession> list = query.getResultList();
        return list == null || list.size() < 1? Optional.empty(): Optional.ofNullable(list.get(0));
	}

	public void merge(UserSession uSession) {
		em.merge(uSession);
	}
}