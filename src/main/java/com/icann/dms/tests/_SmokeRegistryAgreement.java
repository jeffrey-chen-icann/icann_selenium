package com.icann.dms.tests;

import java.util.ArrayList;
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

import com.icann._contentrecordtypes.*;
import com.icann.dms.*;
import com.icann.dms.contenttype.*;
import com.icann.e2e.Suite;

public class _SmokeRegistryAgreement {
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
		
		Helper.logTestGroup("Registry Agreement smoke tests");
		
		Dms.login();		
	}

	@Before
	public void beforeEach(){

	}
	
	@Ignore
	@Test
	public void registryAgreementDefaultFieldValues() {
		String sField;
		List<String> lsExpectedValues;
		
		Helper.logTest("dms.smoke.registryagreement.defaultfields - ITI-3568");

		String sContentType = "Registry Agreement";
		Helper.logTestStep("Create a new item of type:  " + sContentType);
		LandingPage.createContent(sContentType);
		
		sField = "Internal Owner";
		lsExpectedValues = Arrays.asList("Global Domains Division (GDD)");
		Helper.logTestStep("Verify the default value for metadata field:  " + sField);
		Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField), sField + " default value");
		
		sField = "Topic Owner";
		lsExpectedValues = Arrays.asList("Global Domains Division (GDD)");
		Helper.logTestStep("Verify the default value for metadata field:  " + sField);
		Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField), sField + " default value");

		sField = "Team";
		lsExpectedValues = Arrays.asList("Registry Services");
		Helper.logTestStep("Verify the default value for metadata field:  ");
		Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField), sField + " default value");
		
		sField = "Topic";
		lsExpectedValues = Arrays.asList("Registry Agreement");
		Helper.logTestStep("Verify the default value for metadata field:  ");
		Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField), sField + " default value");
		
		sField = "Subtopic";
		lsExpectedValues = Arrays.asList("Individual Registry Agreement");
		Helper.logTestStep("Verify the default value for metadata field:  ");
		Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField), sField + " default value");
			
	}
	
	@Test
	public void createRegistryAgreement() {
		Helper.logTest("dms.smoke.registryagreement.create - ITI-3526");
		
		if (Suite.bUsingSuite) {
			Helper.logDebug("Using suite.");
			for (int i=0; i<lsSuiteVars.size(); i++) {
				try {
					Helper.logDebug("Assigning field name  " + _SmokeRegistryAgreement.class.getField(lsSuiteVars.get(i)) + " to suite value " + Suite.class.getField(lsSuiteVars.get(i)).get(null));
					_SmokeRegistryAgreement.class.getField(lsSuiteVars.get(i)).set(this, Suite.class.getField(lsSuiteVars.get(i)).get(null));
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
		
		raRecord.metadata.sMetadataDescription = raRecord.sGtld + " metadata description text";
		
		if (Suite.bUsingSuite) {
			Suite.raRecord = raRecord;
		}

		Dms.createRegistryAgreement(raRecord);
		
		//hacky - setting bottom fields first so the scrollbar blocking problem (ITI-3297) is not there
		RegistryAgreementPage.setTextForField("Metadata Description", raRecord.metadata.sMetadataDescription);
		RegistryAgreementPage.setDropdownSelection("Agreement Status", "Current");
		RegistryAgreementPage.setDropdownSelection("Agreement Round", "2004");
		
		Helper.logTestStep("Save the item as a draft.");
		RegistryAgreementPage.saveDraft();

		if (Suite.bUsingSuite) {
			Suite.sNodeId = sNodeId;
		}
		
		Helper.logTestStep("Wait 5 seconds (enough for reindexing to occur so the item can be found using search?).");
		Helper.nap(5);
		
		Helper.logTestStep("Go to the edit existing content page.");
		EditExistingContent.open();
		
		Helper.logTestStep("Filter by the gTLD we created:  " + raRecord.sGtld);
		Helper.logMessage("Type the gTLD into the page title field:  " + raRecord.sGtld);
		Helper.waitForThenSendKeys(EditExistingContent.txtPageTitle, raRecord.sGtld);
		
		Helper.logMessage("Click Apply Filters.");
		Helper.waitForThenClick(EditExistingContent.btnApplyFilters);
		
		int iTotalResultRows = EditExistingContent.iTotalResultRows();
		Helper.logMessage("Total results after filtering:  " + iTotalResultRows);		
		
		switch (iTotalResultRows) {
		case 1:
			String sExpectedRowPageTitle = "." + raRecord.sGtld + " Registry Agreement";
			List<String> lsExpectedRowData = Arrays.asList(sExpectedRowPageTitle, "Registry Agreement", "", "Draft");
			List<String> lsActualRowData = EditExistingContent.lsRowWithTitleData(sExpectedRowPageTitle);
			Helper.logTestStep("Verify row information:  " + lsExpectedRowData);
			Helper.compareLists(lsExpectedRowData, lsActualRowData);
			
			Helper.logTestStep("Click edit link on row:  " + sExpectedRowPageTitle);
			String sWindowHandle = browser.getWindowHandle();
			Helper.waitForThenClick(EditExistingContent.btnEditContentLink(sExpectedRowPageTitle));
			ArrayList<String> allTabs = new ArrayList<String>(browser.getWindowHandles());
			if (allTabs.size() > 1) {
				Helper.logMessage("*** New tab detected...switching to new tab and closing old tab.");
				browser.close();
				allTabs.remove(sWindowHandle);
				browser.switchTo().window(allTabs.get(0));
			}
			
			//verify nodeId
			Helper.logMessage("Verify the current URL contains the expected nodeId:  " + raRecord._sNodeId);
			Helper.waitForUrlToContain(raRecord._sNodeId);
			
			//verify item data we set (all?)
//			Helper.logMessage(RegistryAgreementPage.lsExistingSelectionsForField("gTLD/String").toString());
			
			String sField;
			List<String> lsExpectedValues;
			
			sField = "gTLD/String";
			lsExpectedValues = Arrays.asList(raRecord.sGtld);
			sValueToSelect = raRecord.sGtld;
			Helper.logTestStep("Verify the " + sField + " field was populated as expected:  " + sValueToSelect); 
			Helper.compareStrings(sValueToSelect, RegistryAgreementPage.lsExistingSelectionsForField(sField).get(0));
			
			sField = "U-Label";
			sValueToSelect = raRecord.sGtld;
			Helper.logTestStep("Verify the " + sField + " field was populated as expected:  " + sValueToSelect); 
			Helper.compareStrings(sValueToSelect, RegistryAgreementPage.getTextForField(sField));

			sField = "Internal Owner";
			lsExpectedValues = Arrays.asList("Global Domains Division (GDD)");
			Helper.logTestStep("Verify the saved value for metadata field:  " + sField);
			Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField));
			
			sField = "Topic Owner";
			lsExpectedValues = Arrays.asList("Global Domains Division (GDD)");
			Helper.logTestStep("Verify the saved value for metadata field:  " + sField);
			Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField));

			sField = "Team";
			lsExpectedValues = Arrays.asList("Registry Services");
			Helper.logTestStep("Verify the saved value for metadata field:  ");
			Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField));
			
			sField = "Topic";
			lsExpectedValues = Arrays.asList("Registry Agreement");
			Helper.logTestStep("Verify the saved value for metadata field:  ");
			Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField));
			
			sField = "Subtopic";
			lsExpectedValues = Arrays.asList("Individual Registry Agreement");
			Helper.logTestStep("Verify the saved value for metadata field:  ");
			Helper.compareLists(lsExpectedValues, RegistryAgreementPage.lsExistingSelectionsForField(sField));
			
			break;
		default:
			Helper.logError("There were " + iTotalResultRows + " rows displayed after filtering!");
		}

	}


	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
