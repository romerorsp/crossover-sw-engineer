package com.crossover.test.exam.maker.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="answers")
public class Answer implements Serializable {


	private @JsonIgnore @Transient static final long serialVersionUID = 1525102056258081589L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="answer_options",
    		   joinColumns=@JoinColumn(name="answer_id", referencedColumnName="id"),
    		   inverseJoinColumns=@JoinColumn(name="option_id", referencedColumnName="id"))
	private List<Option> options;

	public @JsonProperty Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public @JsonProperty List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
	
	public @JsonProperty Question getQuestion() {
		return options == null || options.size() < 1? null: options.get(0).getQuestion();
	}
}