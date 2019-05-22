package com.icann.dms.tests;

import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;
import java.lang.reflect.Field;

import com.icann._contentrecordtypes.RegistryAgreement;
import com.icann.dms.*;
import com.icann.dms.ContentType.*;
import com.icann.e2e.Suite;

public class DmsPoc {
    static WebDriver browser; 
    String sNodeId = "unset";
	String sWhichField = "";
	String sValueToSelect = "";
	
	
	//list out the variables that are settable by the suite if defined
	List<String> lsSuiteVars = Arrays.asList("sALabel", "sTypeOfTld", "sAgreementType", "sAgreementDate");
	public String sALabel;
	public String sTypeOfTld;
	public String sAgreementType;
	public String sAgreementDate;

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
		Helper.bDebug = true;
		
		if (Suite.bUsingSuite) {
			Helper.logDebug("Using suite.");
			for (int i=0; i<lsSuiteVars.size(); i++) {
				try {
					Helper.logDebug("Assigning field name  " + DmsPoc.class.getField(lsSuiteVars.get(i)) + " to suite value " + Suite.class.getField(lsSuiteVars.get(i)).get(null));
					DmsPoc.class.getField(lsSuiteVars.get(i)).set(this, Suite.class.getField(lsSuiteVars.get(i)).get(null));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			sALabel = Helper.todayString() + "_dms_automation";
			sTypeOfTld = "gTLD ";
			sAgreementType = "Community - Spec 12";
			sAgreementDate = Helper.todayString("dd") + " " + Helper.todayString("Month") + " " + Helper.todayString("yyyy");
		}
		
		Helper.logDebug("sALabel = " + sALabel);
		Helper.logDebug("sTypeOfTld = " + sTypeOfTld); 
		Helper.logDebug("sAgreementType = " + sAgreementType); 
		Helper.logDebug("sAgreementDate = " + sAgreementDate); 
		
		Helper.bDebug = false;
		
		RegistryAgreement raRecord = new RegistryAgreement(sALabel, sAgreementType, "", "Active", sAgreementDate);
		
		if (Suite.bUsingSuite) {
			Suite.raRecord = raRecord;
		}
		
		
		
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
		sNodeId = RegistryAgreementPage.getCurrentNodeId();
		Helper.logMessage("The created nodeId:  " + sNodeId);
		if (Suite.bUsingSuite) {
			Suite.sNodeId = sNodeId;
		}

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
		ContentItem.setDropdownSelection("Type of TLD", sValueToSelect);

		Helper.logTestStep("Check the Part of Registry Agreement checkbox.");
		Helper.waitForThenClick(RegistryAgreementPage.chkPartOfARegistryAgreement);
		
		Helper.logTestStep("Click the Create button.");
		Helper.waitForThenClick(RegistryAgreementPage.btnTldCreate);
				
		sWhichField = "agreement type";
		sValueToSelect = raRecord.sAgreementType;
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		ContentItem.setDropdownSelection(sWhichField, sValueToSelect);
		
		//hacky - setting bottom fields first so the scrollbar blocking problem (ITI-3297) is not there
		String sMetadataDescription = raRecord.sGtld + " metadata description text";
		Helper.logTestStep("Enter text into the Metadata Description:  " + sMetadataDescription);
		Helper.waitForThenSendKeys(Taxonomy.txtMetadataDescription, sMetadataDescription);

		sWhichField = "agreement status";
		sValueToSelect = "Current";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		ContentItem.setDropdownSelection(sWhichField, sValueToSelect);
		
		sWhichField = "agreement round";
		sValueToSelect = "2004";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		ContentItem.setDropdownSelection(sWhichField, sValueToSelect);
		
		Helper.logTestStep("Click the Save Draft button.");
		Helper.waitForThenClick(RegistryAgreementPage.btnSaveDraft);

		Helper.logTestStep("Publish the content item.");
		ContentItem.publishNow();
		
		//poll the published state
		ContentItem.waitForWorkflowState("Published", 60);
		
////>>>somehow trigger cache refresh - graphql?

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
