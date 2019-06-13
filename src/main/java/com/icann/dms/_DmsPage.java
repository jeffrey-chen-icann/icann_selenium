package com.icann.dms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;
import com.icann.Helper;
import com.icann.dms.contenttype.RegistryAgreementPage;

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
		
//		Helper.nap(2);
		
		switch (sWhichField.toLowerCase()) {
		case "type of tld":
			Helper.logDebug("Skipping this type of field population:  " + sWhichField.toLowerCase());
			break;
		default:
			Helper.logTestStep("Verify the " + sWhichField + " field was populated as expected:  " + sValueToSelect); 
			Helper.compareStrings(sValueToSelect, RegistryAgreementPage.lsExistingSelectionsForField(sWhichField).get(0));
		}
		
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
    
    private static String sParentElementRootXpath (String sFieldName) {
    	return ("//*[@id=\"" + sFieldIdentifier(sFieldName) + "\"]/ancestor::mat-form-field");
    }
    public static By txtForField(String sFieldName) {
    	By byControl = null;
    	
    	byControl = By.xpath(sParentElementRootXpath(sFieldName) + "//input");   	
    	
    	return byControl;
    }
    public static By btnDropdownForField(String sFieldName) {
    	By byControl = null;
    	
    	switch (sFieldName.toLowerCase()){
    		case "type of tld":
    			byControl = By.xpath(sParentElementRootXpath(sFieldName) + "//div");
    			break;
    	default:
    		byControl = By.xpath(sParentElementRootXpath(sFieldName) + "//button");
    	}
    	
    	return byControl;
    }
    
    private static By lwExistingSelectionsForField(String sFieldName) {
    	return By.xpath(sParentElementRootXpath(sFieldName) + "//span[@class[contains(.,\"app-typeahead-select-box\")]]");
    }
    public static List<String> lsExistingSelectionsForField(String sFieldName) {
    	List<String> lsSelections =  new ArrayList<String>();
    	
    	Helper.waitForNumberOfElementsToAppear(lwExistingSelectionsForField(sFieldName), 1);
    	
    	for (WebElement e : browser.findElements(lwExistingSelectionsForField(sFieldName))) {
    		String sText = e.getText();
    		lsSelections.add(sText.substring(0, sText.indexOf(" clear")).strip());
    	}
    	
    	return lsSelections;
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
    	case "gtld/string":  //registry agreement
    		sIdentifier = "icn:associatedTLD";
    		break;
    	case "internal owner":  //registry agreement
    		sIdentifier = "icn:internalOwner";
    		break;
    	case "languages":  //request translation
    		sIdentifier = "languages";
    		break;
    	case "reviewer":  //request review
    		sIdentifier = "reviewer";
    		break;
    	case "subtopic":
    		sIdentifier = "icn:subtopic";
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
    		Helper.logError("Need to define fieldname in _DmsPage.sFieldIdentifier():  " + sFieldName);
    	}
    	
    	return sIdentifier;
    }
}
