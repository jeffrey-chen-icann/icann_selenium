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
    
    public static By btnMetadataOverflowChoice(String sChoiceText) {
    	return By.xpath("//*[text()=\"" + sChoiceText + "\"]/ancestor::mat-option");
    }
    public static void setDropdownSelection(String sWhichField, String sValueToSelect) {
    	Helper.logMessage("Click the " + sWhichField + " dropdown link.");
    	
    	switch (sWhichField.toLowerCase()){
		//these are dropdown only fields
    	case "type of tld":
    		Helper.logDebug("We think " + sWhichField + " is a select control.");
			Helper.waitForThenClick(btnDropdownForField(sWhichField));
			break;
		//these are enter/select fields (with a text area)
		default:
			Helper.logDebug("We think " + sWhichField + " is an enter/select control.");
			Helper.waitForThenSendKeys(txtForField(sWhichField), sValueToSelect);
    	}
    	
		Helper.logMessage("Click the popup menu item:  " + sValueToSelect);
		Helper.waitForThenClick(btnMetadataOverflowChoice(sValueToSelect));
		Helper.nap(2);
    }
    
    public static By txtForField(String sFieldName) {
    	By byControl = null;
    	
    	byControl = By.xpath("//*[@id=\"" + sFieldIdentifier(sFieldName) + "\"]/ancestor::mat-form-field//input");   	
    	
    	return byControl;
    }
    public static By btnDropdownForField(String sFieldName) {
    	By byControl = null;
    	
    	switch (sFieldName.toLowerCase()){
    		case "type of tld":
    			byControl = By.xpath("//*[@id=\"" + sFieldIdentifier(sFieldName) + "\"]/ancestor::mat-form-field//div");
    			break;
    	default:
    		byControl = By.xpath("//*[@id=\"" + sFieldIdentifier(sFieldName) + "\"]/ancestor::mat-form-field//button");
    	}
    	
    	
    	return byControl;
    }
    
    static private String sFieldIdentifier(String sFieldName) {
    	String sIdentifier = "unset";
    	
    	switch (sFieldName.toLowerCase()) {
    	case "agreement round":
    		sIdentifier = "icn:raRound";
    		break;
    	case "agreement status":
    		sIdentifier = "icn:raStatus";
    		break;
    	case "agreement type":
    		sIdentifier = "icn:raType";
    		break;
    	case "languages":  //request translation
    		sIdentifier = "languages";
    		break;
    	case "reviewer":  //request review
    		sIdentifier = "reviewer";
    		break;
    	case "team":
    		sIdentifier = "icn:subowner";
    		break;
    	case "topic owner":
    		sIdentifier = "icn:owner";
    		break;
    	case "topic":
    		sIdentifier = "icn:topic";
    		break;
    	case "type of tld":
    		sIdentifier = "icn:typeOfTld";
    		break;
    	default:
    		
    	}
    	
    	return sIdentifier;
    }
    
    public static By txtPageTitle = By.id("mat-input-2"); //edit existing content filter
    public static By txtMetadataDescription = By.id("icn:metadataDescription"); //content pages 
}
