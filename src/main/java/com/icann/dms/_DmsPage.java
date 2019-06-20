package com.icann.dms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;
import com.icann.Helper;

public class _DmsPage extends _DmsHeader {
    static WebDriver browser = Helper.browser;
    
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
    
    public static By popupMenuItems = By.xpath("//mat-option");
    public static List<String> lsDropdownSelections(String sWhichField) {
    	List<String> lsSelections =  new ArrayList<String>();
    	
    	Helper.logMessage("Click the " + sWhichField + " dropdown link.");
    	
    	Helper.waitForThenClick(btnDropdownForField(sWhichField));  
    	
    	Helper.logDebug("Waiting for popup items...");
    	Helper.waitForNumberOfElementsToAppear(popupMenuItems, 1);
    	
    	List<WebElement> lwMenuItems = browser.findElements(popupMenuItems);
    	for (WebElement e : lwMenuItems) {
    		Helper.logDebug("item:  " + e.getText());
    		lsSelections.add(e.getText().strip());
    	}
    	
    	Helper.logMessage("Send an escape key to the " + sWhichField + " dropdown link.");
    	Helper.waitForElement(txtForField(sWhichField)).sendKeys(Keys.ESCAPE);;  
    	
    	return lsSelections;
    }
    
    public static By txtForField(String sFieldName) {
    	By byControl = null;
    	
    	byControl = By.xpath("//*[@id=\"" + sFieldIdentifier(sFieldName) + "\"]/ancestor::mat-form-field//input");   	
    	
<<<<<<< Updated upstream
    	return byControl;
    }
=======
    	return (sModalPre + "//*[@id=\"" + sFieldIdentifier(sFieldName) + "\"]/ancestor::mat-form-field");
    }
    private static By txtForField(String sFieldName) {
    	By byControl = null;
    	switch (sFieldName.toLowerCase()) {
    	case sModalMetadataField:  //file metadata
    	case "metadata description":
    		byControl = By.xpath(sParentElementRootXpath(sFieldName) + "//textarea");
    		break;
    	case "status":
    		byControl = By.xpath(sParentElementRootXpath(sFieldName) + "//*[@class[contains(.,\"mat-select-value-text\")]]");
    		break;
    	default:
    		byControl = By.xpath(sParentElementRootXpath(sFieldName) + "//input");
    	}
    	   	
    	return byControl;
    }
    public static String getTextForField(String sFieldName) {
    	String sReturn = "unset";
    	
    	switch (sFieldName.toLowerCase()) {
    	// these fields are special because they are disabled
    	case "u-label":
    	case "page title":
    		sReturn = Helper.waitForDisabledElement(txtForField(sFieldName)).getAttribute("value");
    		break;
    	default:
    		sReturn = Helper.waitForElement(txtForField(sFieldName)).getAttribute("value");
    	}
    	return sReturn;
    }
>>>>>>> Stashed changes
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
    	case "agreement round":  //registry agreement
    		sIdentifier = "icn:raRound";
    		break;
    	case "agreement status":  //registry agreement
    		sIdentifier = "icn:raStatus";
    		break;
    	case "agreement type":  //registry agreement
    		sIdentifier = "icn:raType";
    		break;
    	case "board meeting type":  //board meeting
    		sIdentifier = "icn:legalBoardMeetingType";
    		break;
    	case "languages":  //request translation
    		sIdentifier = "languages";
    		break;
<<<<<<< Updated upstream
=======
    	case "legal case value":  //independent review process
    		sIdentifier = "icn:legalCase";
    		break;
    	case "legal case status":  //independent review process
    		sIdentifier = "icn:legalCaseStatus";
    		break;
    	case "legal document type":  //board meeting
    		sIdentifier = "icn:legalDocumentType";
    		break;    		
    	case "legal document year":  //board meeting
    		sIdentifier = "icn:legalDocYear";
    		break;    		
    	case "metadata description":  //registry agreement
    		sIdentifier = "icn:metadataDescription";
    		break;
    	case "name":  //add file via search modal
    		sIdentifier = "cm:name";
    		break;
    	case "u-label":  //registry agreement
    		sIdentifier = "icn:uLabel";
    		break;
    	case "page title":  //many
    		sIdentifier = "icn:pageTitle";
    		break;
>>>>>>> Stashed changes
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
    	case "type of tld":  //registry agreement
    		sIdentifier = "icn:typeOfTld";
    		break;
    	default:
    		
    	}
    	
    	return sIdentifier;
    }
}
