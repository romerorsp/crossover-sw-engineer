package com.crossover.test.exam.maker.security;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.ws.rs.HeaderParam;

public class UserCredentials  implements Serializable {
	
	private static final long serialVersionUID = 4683323863718576903L;

	private @NotNull @HeaderParam(value="l") String login;
	
	private @NotNull @HeaderParam(value="p") String pwd;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}	
}
