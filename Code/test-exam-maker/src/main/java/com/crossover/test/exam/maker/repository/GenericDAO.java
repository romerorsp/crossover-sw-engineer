package com.crossover.test.exam.maker.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public abstract class GenericDAO<T, ID extends Serializable> implements CrudRepository<T, ID> {

	protected @Autowired EntityManager em;

	protected abstract Class<T> getBeanType();

	@Override
	public long count() {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		return em.createQuery(cq.select(qb.count(cq.from(getBeanType())))).getSingleResult();
	}

	@Override
	public void delete(ID id) {
		em.remove(this.findOne(id));
	}

	@Override
	public void delete(T bean) {
		em.remove(bean);
		em.flush();
	}

	@Override
	public void delete(Iterable<? extends T> it) {
		it.forEach(this::delete);
	}

	@Override
	public void deleteAll() {
		delete(this.findAll());
	}

	@Override
	public boolean exists(ID id) {
		return findOne(id) != null;
	}

	@Override
	public Iterable<T> findAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		Class<T> type = getBeanType();
		CriteriaQuery<T> cq = cb.createQuery(type);
		Root<T> rootEntry = cq.from(type);
		CriteriaQuery<T> all = cq.select(rootEntry);
		TypedQuery<T> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	public Iterable<T> findAll(Iterable<ID> ids) {
		List<T> result = new ArrayList<>();
		ids.forEach(id -> result.add(findOne(id)));
		return result.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public T findOne(ID id) {
		return em.find(getBeanType(), id);
	}

	@Override
	public <S extends T> S save(S bean) {
		em.persist(bean);
		em.flush();
		return bean;
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> it) {
		it.forEach(this::save);
		return it;
	}
}