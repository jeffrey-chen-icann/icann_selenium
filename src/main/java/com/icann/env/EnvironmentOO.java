package com.icann.env;

import com.icann.env.Dev;
import com.icann.Environment;

public interface EnvironmentOO{
	default String sDmsUrl() {
		String sDmsUrl;
		
		if (Environment.sEnvironment.contentEquals("prod")) {
			sDmsUrl = "http://iti-adf.icann.org";
		} else {
			sDmsUrl = String.format("http://iti-adf-%s.icann.org", Environment.sEnvironment);
		}
		
		return sDmsUrl;
	}
	public String sDmsAdminUsername();
	public String sDmsAdminPassword();
	public String sDmsNonAdminUsername();
	public String sDmsNonAdminPassword();
	
//STATICS
	
	public static EnvironmentOO setEnvironment(String sEnv){
		Environment.sEnvironment = sEnv;
		
		EnvironmentOO env = null;
		
		switch (Environment.sEnvironment) {
		case "dev":
			env = new Dev();
			break;
		case "staging":
			env = new Staging();
			break;
		}	
		return env;
	}
	public static EnvironmentOO setEnvironment() {
		return setEnvironment("dev");
	}

}

