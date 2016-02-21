package com.crossover.test.exam.maker.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="options")
public class Option implements Serializable {

	private @JsonIgnore @Transient static final long serialVersionUID = -4997367395698046520L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 

	private @Column(name="description") String description;
	
	private @Column(name="short_name", nullable=false, unique=true, length=10) String shortName;
	
	private @Column(name="correct", nullable=false) boolean correct;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="question_id")
	private Question question;

	public @JsonProperty Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public @JsonProperty String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public @JsonProperty boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public @JsonIgnore Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public @JsonProperty String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (correct ? 1231 : 1237);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((shortName == null) ? 0 : shortName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Option other = (Option) obj;
		if (correct != other.correct)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		return true;
	}
}