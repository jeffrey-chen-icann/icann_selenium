package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class Dms {
    static WebDriver browser = Helper.browser;
    
    public static void login(String sUsername, String sPassword){
    	Login.login(sUsername, sPassword);
    }
    public static void login(){
    	Login.login();
    }
	public static void logout() {
		_DmsHeader.logout();
	}
    


    public static String sPermissionsError = "This file or folder no longer exists or you don't have permission to view it.\n";
    public static By txtPermissionsError = Helper.anythingWithText(sPermissionsError);
}
