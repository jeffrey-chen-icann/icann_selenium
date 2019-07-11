package com.icann.env;

import com.icann.Environment;

public class Dev implements EnvironmentOO {

	public Dev() {
		Environment.sEnvironment = "dev";
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
		return "test@test.com";
	}

	@Override
	public String sDmsNonAdminPassword() {
		return "JeffIsTheBest";
	}

}
