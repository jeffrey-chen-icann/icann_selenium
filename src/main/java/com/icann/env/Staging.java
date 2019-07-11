package com.icann.env;

import com.icann.Environment;

public class Staging implements EnvironmentOO {

	public Staging() {
		Environment.sEnvironment = "staging";
	}
	
	@Override
	public String sDmsAdminUsername() {
		return "admin@app.activiti.com";
	}

	@Override
	public String sDmsAdminPassword() {
		return "admin";
	}
	@Override
	public String sDmsNonAdminUsername() {
		return "unset";
	}

	@Override
	public String sDmsNonAdminPassword() {
		return "unset";
	}

}
