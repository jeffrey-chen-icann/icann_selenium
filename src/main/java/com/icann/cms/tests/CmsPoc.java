package com.icann.cms.tests;

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

import com.icann.cms.*;
import com.icann.e2e.Suite;

public class CmsPoc {
    static WebDriver browser; 
    String sNodeId = "unset";
	String sWhichField = "";
	String sValueToSelect = "";
    
//	List<String> lsSuiteVars = Arrays.asList("sALabel", "sTypeOfTld", "sAgreementType", "sAgreementDate");
	List<String> lsSuiteVars = Arrays.asList("raRecord");
	public String sALabel;
	public String sTypeOfTld;
	public String sAgreementType;
	public String sAgreementDate;
	public RegistryAgreement raRecord;
	
	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers("dev");	
		
		Helper.logTestGroup("Proof of concept - CMS POC");	
	}

	@Before
	public void beforeEach(){
		
	}
	
	@Test
	public void retrieveRegistryAgreement() {		
		if (Suite.bUsingSuite) {
			Helper.logDebug("Using suite.");
			for (int i=0; i<lsSuiteVars.size(); i++) {
				try {
					Helper.logDebug("Assigning field name  " + CmsPoc.class.getField(lsSuiteVars.get(i)) + " to suite value " + Suite.class.getField(lsSuiteVars.get(i)).get(null));
					CmsPoc.class.getField(lsSuiteVars.get(i)).set(this, Suite.class.getField(lsSuiteVars.get(i)).get(null));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			sALabel = Helper.todayString() + "_cms_automation";
			sTypeOfTld = "gTLD ";
			sAgreementType = "Community - Spec 12";
			sAgreementDate = Helper.todayString("dd") + " " + Helper.todayString("Month") + " " + Helper.todayString("yyyy");
			
			raRecord = new RegistryAgreement(sALabel, sAgreementType, "", "Active", sAgreementDate);
		}
		
		Helper.logDebug("sALabel = " + sALabel);
		Helper.logDebug("sTypeOfTld = " + sTypeOfTld); 
		Helper.logDebug("sAgreementType = " + sAgreementType); 
		Helper.logDebug("sAgreementDate = " + sAgreementDate); 
		Helper.logDebug("raRecord = " + raRecord.toString());
		Helper.bDebug = false;
		
		String sContentType = "Registry Agreement";
		
//		//debug shortcut
//		raRecord.sGtld = "20190520_13.25.09_dms_automation"; 
//		sALabel = raRecord.sGtld;
		
		
		
		Helper.logTest("Look up new DMS content in CMS:  " + sContentType);
		Helper.logMessage("Looking for registry agreement:  " + raRecord.sGtld);
		Helper.logMessage("");
		
		Helper.logTestStep("Open the registry agreements page.");
		RegistryAgreementsPage.open();
		
		sWhichField = "gTLD / String";
		Helper.logTestStep("Filter the registry agreements by the new A-Label:  " + raRecord.sGtld);
		Helper.logDebug("Click the " + sWhichField + " text field.");
		Helper.waitForThenClick(RegistryAgreementsPage.txtEnterSelect(sWhichField));
		Helper.nap(2);
		
		Helper.waitForThenSendKeys(RegistryAgreementsPage.txtEnterSelectSearch("gTLD / String"), raRecord.sGtld);
		
		Helper.logTestStep("Click the checkbox for filter item:  " + raRecord.sGtld);
		Helper.waitForThenClick(RegistryAgreementsPage.chkFilterSelection(raRecord.sGtld));
		
		Helper.logTestStep("Click Apply.");
		Helper.waitForThenClick(RegistryAgreementsPage.btnApplyFilters);
		Helper.nap(1);
		
		List<String> lsExpected = Arrays.asList(raRecord.sGtld, raRecord.sAgreementType, raRecord.sOperator, raRecord.sAgreementStatus, raRecord.sAgreementDate);
		Helper.logMessage("Verify the row record information.");
		Helper.compareLists(lsExpected, RegistryAgreementsPage.searchResultRowValue(1).toList());
	
		Helper.logTestStep("Click the agreement link with text:  " + raRecord.sGtld);
		Helper.waitForThenClick(RegistryAgreementsPage.resultLink(raRecord.sGtld));
		
		Helper.logTestStep("Verify the returned URL contains:  " + raRecord.sGtld);
		Helper.waitForUrlToContain(raRecord.sGtld);
		
		String sExpectedAgreementTitle = "." + raRecord.sGtld + " Registry Agreement";
		Helper.logTestStep("Verify the agreement title is:  " + sExpectedAgreementTitle);
		Helper.compareStrings(sExpectedAgreementTitle, Helper.waitForElement(RegistryAgreementDetail.txtAgreementTitle).getText());
		
		String sULabelPreString = "U-Label ";
		Helper.logTestStep("Verify the U-Label is:  " + raRecord.sGtld);
		Helper.compareStrings(sULabelPreString + raRecord.sGtld, Helper.waitForElement(RegistryAgreementDetail.txtULabel).getText());
		
		String sOperatorPreString = "Operator";
		Helper.logTestStep("Verify the Operator is:  " + raRecord.sOperator);
		Helper.compareStrings(sOperatorPreString + raRecord.sOperator, Helper.waitForElement(RegistryAgreementDetail.txtOperator).getText());

		String sAgreementDatePreString = "Agreement Date ";
		Helper.logTestStep("Verify the Agreement Date is:  " + raRecord.sAgreementDate);
		Helper.compareStrings(sAgreementDatePreString + raRecord.sAgreementDate, Helper.waitForElement(RegistryAgreementDetail.txtAgreementDate).getText());
		
		String sAgreementTypePreString = "Agreement Type ";
		Helper.logTestStep("Verify the Agreement Type is:  " + raRecord.sAgreementType);
		Helper.compareStrings(sAgreementTypePreString + raRecord.sAgreementType, Helper.waitForElement(RegistryAgreementDetail.txtAgreementType).getText());

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
