package com.crossover.test.exam.maker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Exam;

@Component
public class ExamDAO extends GenericDAO<Exam, Integer> {

	@Override
	protected Class<Exam> getBeanType() {
		return Exam.class;
	}

	public Optional<Exam> findByShortName(String shortName) {
		List<Exam> result = em.createQuery("FROM Exam e WHERE e.shortName = :shortName", getBeanType()).setParameter("shortName", shortName).getResultList();
		return result.isEmpty()? Optional.empty(): Optional.of(result.get(0));
	}
}
