package com.crossover.test.exam.maker.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crossover.test.exam.maker.bean.Option;
import com.crossover.test.exam.maker.repository.OptionDAO;

@Component
public class OptionBusiness {

	private @Autowired OptionDAO dao;
	
	public Optional<Option> getOption(Long id) {
		try {
			return Optional.ofNullable(dao.findOne(id));
		} catch(Exception e) {
			return Optional.empty();
		}
	}
}