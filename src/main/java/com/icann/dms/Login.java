package com.icann.dms;

import org.openqa.selenium.*;

import com.icann.Environment;
import com.icann.Helper;

public class Login {
    static WebDriver browser = Helper.browser;
    
    public static By txtUsername = By.id("username");
    public static By txtPassword = By.id("password");
    public static By btnSignInButton = By.id("login-button");
    
    public static void login(String sUsername, String sPassword){
    	browser = Helper.browser;    	
    	browser.get(Environment.sDmsUrl());
    	
    	fillOutLogin(sUsername, sPassword);
    	
    	Helper.logDebug("Click Sign In button.");
        Helper.waitForThenClick(btnSignInButton);
    	
        Helper.logDebug("Waiting for landing page...");
        Helper.waitForElement(DmsHeader.btnCreateContent);
    }
    
    public static void login(){
    	login(Environment.sDmsAdminUsername(), Environment.sDmsAdminPassword());
    }
    
	public static void logout(){

	}
    
    public static void fillOutLogin(String sUsername, String sPassword){
    	Helper.logDebug("Entering in username/password:  " + sUsername + "/" + sPassword);
        Helper.waitForThenSendKeys(txtUsername, sUsername);
        Helper.waitForThenSendKeys(txtPassword, sPassword);
    }
}