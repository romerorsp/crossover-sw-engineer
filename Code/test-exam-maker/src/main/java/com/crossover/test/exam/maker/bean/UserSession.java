package com.crossover.test.exam.maker.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="user_session")
public class UserSession implements Serializable {


	private @JsonIgnore @Transient static final long serialVersionUID = -3800251688523901662L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private ExamStatus examStatus;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", unique= true, nullable=true, insertable=true, updatable=true)
	private User user;

	@Column(name="started")
	@Temporal(TemporalType.TIMESTAMP)
	private Date starting;
	
	private @Column(name="duration_millis") Long duration;

	public @PrePersist void preInsert() {
		if(examStatus == null) {
			 examStatus = ExamStatus.READY;
		}
	}
	
	public @JsonProperty Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public @JsonProperty ExamStatus getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(ExamStatus examStatus) {
		this.examStatus = examStatus;
	}

	public @JsonProperty Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public @JsonProperty Date getStarting() {
		return starting;
	}

	public void setStarting(Date starting) {
		this.starting = starting;
	}

	public @JsonIgnore User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public @JsonProperty long getRemaining() {
		return (starting.getTime() + duration) - System.currentTimeMillis();
	}
}