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
}
