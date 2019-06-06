package com.icann.dms.tests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;

import com.icann._contentrecordtypes.RegistryAgreement;
import com.icann.dms.*;
import com.icann.dms.contenttype.*;

public class DmsPocOriginal {
    static WebDriver browser; 
    String sNodeId = "unset";
	String sWhichField = "";
	String sValueToSelect = "";
    
	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers("dev");	
		
		Helper.logTestGroup("Proof of concept - DMS ===> CMS");
		
		Dms.login();		
	}

	@Before
	public void beforeEach(){
		
	}
	
	@Test
	public void createRegistryAgreement() {
		String sALabel = Helper.todayString() + "_dms_automation";
		String sTypeOfTld = "gTLD ";
		String sAgreementType = "Community - Spec 12";
		String sAgreementDate = Helper.todayString("dd") + " " + Helper.todayString("Month") + " " + Helper.todayString("yyyy");
		RegistryAgreement raRecord = new RegistryAgreement(sALabel, sTypeOfTld, sAgreementType, "", "Active", sAgreementDate);
		
//		String sOriginalTab = browser.getWindowHandle();
		
		String sContentType = "Registry Agreement";
		Helper.logTest("Create DMS content:  " + sContentType);

		Helper.logTestStep("Create a new item of type:  " + sContentType);
		LandingPage.createContent(sContentType);
		
		Helper.logTestStep("Wait for registry agreement page.");
		Helper.waitForUrlToContain("registry-agreement");
		Helper.nap(2);
		
		Helper.logTestStep("Click the Save Draft button.");
		Helper.waitForThenClick(RegistryAgreementPage.btnSaveDraft);
		Helper.waitForUrlToContain("nodeId");
		
		Helper.logTestStep("Verify a nodeId is returned.");
		sNodeId = RegistryAgreementPage.currentNodeId();
		Helper.logMessage("The created nodeId:  " + sNodeId);

		Helper.logTestStep("Click Manage on the gTLD/String field.");
		Helper.waitForThenClick(RegistryAgreementPage.btnManageGtldString);
		
		Helper.logTestStep("Set the A-Label:  " + raRecord.sGtld);
		Helper.waitForThenSendKeys(RegistryAgreementPage.txtTldALabel, raRecord.sGtld);

		String sULabel = raRecord.sGtld;
		Helper.logTestStep("Set the U-Label:  " + sULabel);
		Helper.waitForThenSendKeys(RegistryAgreementPage.txtTldULabel, sULabel);

		sWhichField = "type of tld";
		sValueToSelect = sTypeOfTld;
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection("Type of TLD", sValueToSelect);

		Helper.logTestStep("Check the Part of Registry Agreement checkbox.");
		Helper.waitForThenClick(RegistryAgreementPage.chkPartOfARegistryAgreement);
		
		Helper.logTestStep("Click the Create button.");
		Helper.waitForThenClick(RegistryAgreementPage.btnTldCreate);
				
		sWhichField = "agreement type";
		sValueToSelect = raRecord.sAgreementType;
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);
		
		//hacky - setting bottom fields first so the scrollbar blocking problem (ITI-3297)is not there
		String sMetadataDescription = Helper.todayString() + " metadata description text";
		Helper.logTestStep("Enter text into the Metadata Description:  " + sMetadataDescription);
		Helper.waitForThenSendKeys(RegistryAgreementPage.txtMetadataDescription, sMetadataDescription);

		sWhichField = "agreement status";
		sValueToSelect = "Current";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);
		
		sWhichField = "agreement round";
		sValueToSelect = "2004";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);
		
		Helper.logTestStep("Click the Save Draft button.");
		Helper.waitForThenClick(RegistryAgreementPage.btnSaveDraft);

		Helper.logTestStep("Publish the content item.");
		_DmsContentItem.publishNow();
		
		//poll the published state
		_DmsContentItem.waitForWorkflowState("Published", 60);
		
		
////>>>somehow trigger cache refresh - graphql?
//		
//		
////		//debug shortcut
////		raRecord.sGtld = "20190520_13.25.09_dms_automation"; 
////		sALabel = raRecord.sGtld;
//		
//		
//		
//		Helper.logTest("Look up new DMS content in CMS:  " + sContentType);
//		Helper.logMessage("Looking for registry agreement:  " + raRecord.sGtld);
//		Helper.logMessage("");
//		
//		Helper.logTestStep("Open the registry agreements page.");
//		RegistryAgreementsPage.open();
//		
//		sWhichField = "gTLD / String";
//		Helper.logTestStep("Filter the registry agreements by the new A-Label:  " + sALabel);
//		Helper.logDebug("Click the " + sWhichField + " text field.");
//		Helper.waitForThenClick(RegistryAgreementsPage.txtEnterSelect(sWhichField));
//		Helper.nap(2);
//		
//		Helper.waitForThenSendKeys(RegistryAgreementsPage.txtEnterSelectSearch("gTLD / String"), sALabel);
//		
//		Helper.logTestStep("Click the checkbox for filter item:  " + sALabel);
//		Helper.waitForThenClick(RegistryAgreementsPage.chkFilterSelection(sALabel));
//		
//		Helper.logTestStep("Click Apply.");
//		Helper.waitForThenClick(RegistryAgreementsPage.btnApplyFilters);
//		Helper.nap(1);
//		
//		List<String> lsExpected = Arrays.asList(raRecord.sGtld, raRecord.sAgreementType, raRecord.sOperator, raRecord.sAgreementStatus, raRecord.sAgreementDate);
//		Helper.logMessage("Verify the row record information.");
//		Helper.compareLists(lsExpected, RegistryAgreementsPage.searchResultRowValue(1).toList());
//	
//		Helper.logTestStep("Click the agreement link with text:  " + raRecord.sGtld);
//		Helper.waitForThenClick(RegistryAgreementsPage.resultLink(raRecord.sGtld));
//		
//		Helper.logTestStep("Verify the returned URL contains:  " + raRecord.sGtld);
//		Helper.waitForUrlToContain(raRecord.sGtld);
//		
//		String sExpectedAgreementTitle = "." + raRecord.sGtld + " Registry Agreement";
//		Helper.logTestStep("Verify the agreement title is:  " + sExpectedAgreementTitle);
//		Helper.compareStrings(sExpectedAgreementTitle, Helper.waitForElement(RegistryAgreementDetail.txtAgreementTitle).getText());
//		
//		String sULabelPreString = "U-Label ";
//		Helper.logTestStep("Verify the U-Label is:  " + raRecord.sGtld);
//		Helper.compareStrings(sULabelPreString + raRecord.sGtld, Helper.waitForElement(RegistryAgreementDetail.txtULabel).getText());
//		
//		String sOperatorPreString = "Operator";
//		Helper.logTestStep("Verify the Operator is:  " + raRecord.sOperator);
//		Helper.compareStrings(sOperatorPreString + raRecord.sOperator, Helper.waitForElement(RegistryAgreementDetail.txtOperator).getText());
//
//		String sAgreementDatePreString = "Agreement Date ";
//		Helper.logTestStep("Verify the Agreement Date is:  " + raRecord.sAgreementDate);
//		Helper.compareStrings(sAgreementDatePreString + raRecord.sAgreementDate, Helper.waitForElement(RegistryAgreementDetail.txtAgreementDate).getText());
//		
//		String sAgreementTypePreString = "Agreement Type ";
//		Helper.logTestStep("Verify the Agreement Type is:  " + raRecord.sAgreementType);
//		Helper.compareStrings(sAgreementTypePreString + raRecord.sAgreementType, Helper.waitForElement(RegistryAgreementDetail.txtAgreementType).getText());

	}

	@Ignore
	@Test
	public void publishPlayground() {
		browser.navigate().to("https://iti-adf-dev.icann.org/content?nodeId=112b6283-9fb0-473e-9be2-3fb196d44bea");
		
		
		
		
//		RegistryAgreementPage.requestReview("jeffrey.chen");
//		RegistryAgreementPage.requestTranslation("Arabic");
	
	}
	@AfterClass
	static public void cleanup(){
//		Helper.thatsThat();
	}
}
