package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class Taxonomy {
    static WebDriver browser = Helper.browser;
    
    public static By txtMetadataDescription = By.id("icn:metadataDescription"); 
    
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
    

}
