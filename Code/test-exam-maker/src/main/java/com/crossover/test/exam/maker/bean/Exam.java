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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="exams")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Exam implements Serializable {

	private @JsonIgnore @Transient static final long serialVersionUID = -1552208880274713971L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private @Column(name="short_name", nullable=false, unique=true, length=10) String shortName;
	
	private @Column(name="description", nullable=false) String description;
	
	private @Column(name="duration_millis") Long duration;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="exam_id", referencedColumnName="id")
	private List<Question> questions;

	public @JsonProperty Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public @JsonProperty Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public @JsonProperty List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public @JsonProperty String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public @JsonProperty String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
