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

import com.icann._contentrecordtypes.RegistryAgreement;
import com.icann.dms.*;
import com.icann.dms.contenttype.*;
import com.icann.e2e.Suite;

public class DmsPoc {
    static WebDriver browser; 
    String sNodeId = "unset";
	String sWhichField = "";
	String sValueToSelect = "";
	
	
	//list out the variables that are settable by the suite if defined - could this list move to the Suite class and called with the test class name?  hmmmm
	List<String> lsSuiteVars = Arrays.asList("sALabel", "sTypeOfTld", "sAgreementType", "sAgreementDate");
	public String sALabel;
	public String sTypeOfTld;
	public String sAgreementType;
	public String sAgreementDate;

	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers();	
		
		Helper.logTestGroup("Proof of concept - DMS");
		
		Dms.login();		
	}

	@Before
	public void beforeEach(){
		
	}
	
	@Test
	public void createRegistryAgreement() {
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
		
		RegistryAgreement raRecord = new RegistryAgreement(sALabel, sTypeOfTld, sAgreementType, "", "Active", sAgreementDate);
		
		if (Suite.bUsingSuite) {
			Suite.raRecord = raRecord;
		}
		
//		String sOriginalTab = browser.getWindowHandle();
		
		String sContentType = "Registry Agreement";
		Helper.logTest("Create DMS content:  " + sContentType);

		Helper.logTestStep("Create a new item of type:  " + sContentType);
		LandingPage.createContent(sContentType);

		Helper.logTestStep("Save the item as a draft.");
		RegistryAgreementPage.saveDraft();
		
		Helper.logTestStep("Verify a nodeId is returned.");
		sNodeId = RegistryAgreementPage.currentNodeId();
		Helper.logMessage("The created nodeId:  " + sNodeId);
		if (Suite.bUsingSuite) {
			Suite.sNodeId = sNodeId;
		}

		Helper.logTestStep("Create a gTLD/String.");
		RegistryAgreementPage.createGtldString(raRecord);
				
		sWhichField = "Agreement Type";
		sValueToSelect = raRecord.sAgreementType;
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);
		
		//hacky - setting bottom fields first so the scrollbar blocking problem (ITI-3297) is not there
		String sMetadataDescription = raRecord.sGtld + " metadata description text";
		Helper.logTestStep("Enter text into the Metadata Description:  " + sMetadataDescription);
		RegistryAgreementPage.setTextForField("Metadata Description", sMetadataDescription);

		sWhichField = "Agreement Status";
		sValueToSelect = "Current";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);
		
		sWhichField = "Agreement Round";
		sValueToSelect = "2004";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);
		
		Helper.logTestStep("Save the item as a draft.");
		RegistryAgreementPage.saveDraft();

		Helper.logTestStep("Publish the content item.");
		RegistryAgreementPage.publishNow();
		
////>>>somehow trigger cache refresh - graphql?

	}

	@Ignore
	@Test
	public void playground() {
		browser.navigate().to("https://iti-adf-dev.icann.org/content?nodeId=112b6283-9fb0-473e-9be2-3fb196d44bea");
		

		
//		RegistryAgreementPage.requestReview("jeffrey.chen");
//		RegistryAgreementPage.requestTranslation("Arabic");
	
	}
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
