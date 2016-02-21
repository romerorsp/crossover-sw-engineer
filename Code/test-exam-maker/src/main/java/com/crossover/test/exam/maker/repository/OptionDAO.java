package com.crossover.test.exam.maker.repository;

import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Option;

@Component
public class OptionDAO extends GenericDAO<Option, Long> {

	@Override
	protected Class<Option> getBeanType() {
		return Option.class;
	}
}