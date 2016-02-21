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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.crossover.test.exam.maker.CryptoUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="users")
public class User implements Serializable {

	private @JsonIgnore @Transient static final long serialVersionUID = 1514496567756961363L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private @Column(name="name") String name;

	private @Column(name="login") String login;

	private @JsonIgnore @Column(name="pwd") String pwd;

	@OneToOne (cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="exam_id", unique= true, nullable=true, insertable=true, updatable=true)
	private Exam availableExam;

	@OneToMany(cascade={CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval=true, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName="id")
	private List<Answer> answers;
	
	public @JsonProperty Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public @JsonProperty String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public @JsonProperty String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public @JsonIgnore String getPwd() {
		return pwd;
	}

	public @JsonIgnore void setStringPwd(String pwd) {
		this.pwd = CryptoUtil.hash(pwd);
	}

	public @JsonProperty(required=false) Exam getAvailableExam() {
		return availableExam;
	}

	public void setAvailableExam(Exam availableExam) {
		this.availableExam = availableExam;
	}

	public @JsonProperty(required=false) List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		if(this.answers != null) {
			this.answers.clear();
			if(answers != null) {
				this.answers.addAll(answers);
			}
		}  else {
			this.answers = answers;
		}
	}
}