package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;
import com.icann._contentrecordtypes.*;
import com.icann.dms.contenttype.*;

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
    
    
    public static void createIRPLandingPage() {
		String sContentType = "IRP Landing";
		Helper.logTestStep("Create a new item of type:  " + sContentType);
		LandingPage.createContent(sContentType);
    	
    }
    		
    public static String createRegistryAgreement(RegistryAgreement raRecord) {
    	String sWhichField;
    	String sValueToSelect;

		String sContentType = "Registry Agreement";
		Helper.logTestStep("Create a new item of type:  " + sContentType);
		LandingPage.createContent(sContentType);

		Helper.logTestStep("Create a gTLD/String.");
		RegistryAgreementPage.createGtldString(raRecord);

		Helper.logTestStep("Save the item as a draft.");
		RegistryAgreementPage.saveDraft();
		raRecord._sNodeId = RegistryAgreementPage.currentNodeId();
		Helper.logMessage("Created nodeId:  " + raRecord._sNodeId);
		Helper.logMessage("page URL:  " + browser.getCurrentUrl());
		
		sWhichField = "Agreement Type";
		sValueToSelect = raRecord.sAgreementType;
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);
		
    	return raRecord._sNodeId;
    }

}
