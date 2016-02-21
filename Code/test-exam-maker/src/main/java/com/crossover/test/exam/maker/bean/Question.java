package com.crossover.test.exam.maker.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="questions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Question implements Serializable {

	private @JsonIgnore @Transient static final long serialVersionUID = 7166132642352791541L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private QuestionType type;
	
	private @Column(name="ask") String ask;
	
	private @Column(name="short_name", nullable=false, unique=true, length=10) String shortName;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="question_id", referencedColumnName="id")
	private List<Option> options;

	public @JsonProperty Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public @JsonProperty QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public @JsonProperty String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public @JsonProperty List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
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
		result = prime * result + ((ask == null) ? 0 : ask.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		final int staticResult = result;
		result = (options == null)? prime * result: options.stream().map(option -> prime * staticResult + option.hashCode()).reduce(1, (a, b) -> a*b);
		result = prime * result
				+ ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Question other = (Question) obj;
		if (ask == null) {
			if (other.ask != null)
				return false;
		} else if (!ask.equals(other.ask))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (options.size() != other.options.size() || !options.containsAll(other.options))
			return false;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}