package com.icann.dms.tests;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;

import com.icann.dms.*;
import com.icann.dms.contenttype.*;

public class _SmokeIRP {
    static WebDriver browser; 
    String sField;
    String sValue;

	//list out the variables that are settable by the suite if defined - could this list move to the Suite class and called with the test class name?  hmmmm


	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers();	
		
		Helper.logTestGroup("Independent Review Process smoke tests");
				
		Dms.login();				
		
		
		browser.navigate().to("https://iti-adf-dev.icann.org/content?nodeId=091b1dd3-b149-4cb3-937b-69b9ba22557e");
		

		String sFilename = "test.png";

//		IRPPage.addFileToLinkedItemsWithUpload(sFilename);
		IRPPage.addFileToLinkedItemsWithSearch(sFilename);
		
		Helper.bDebug = true;
		
		Helper.logTestStep("Verify the new item exists in the list of linked items:  " + sFilename);
		Helper.logMessage("Linked items:  " + IRPPage.lsLinkedItems().toString());
		Helper.compareStrings("true", Boolean.toString(IRPPage.lsLinkedItems().contains(sFilename)));
		
		
		System.exit(23);
	}

	@Before
	public void beforeEach(){
		
	}
	@Test
	public void createIndependentReviewProcess() {
		Helper.logTest("IRP linked file bug - ITI-3472");
		
		LandingPage.createContent("Independent Review Process");
		
		Helper.logTestStep("Click Manage on the Case Name field.");
		Helper.waitForThenClick(IRPPage.btnManageCaseName);
		
		
		Helper.logTestStep("Fill out the Case Name fields.");
		
		sField = _DmsPage.sModalPrefix + "Topic Owner";
		sValue = "Legal";
		Helper.logTestStep("Enter a value into the field " + sField + ":  " + sValue);
		IRPPage.setDropdownSelection(sField, sValue);

		sField = _DmsPage.sModalPrefix + "Team";
		sValue = "Accountability Mechanism";
		Helper.logTestStep("Enter a value into the field " + sField + ":  " + sValue);
		IRPPage.setDropdownSelection(sField, sValue);
		
		sField = _DmsPage.sModalPrefix + "Topic";
		String sTopic = "Independent Review Process (IRP)";
		Helper.logTestStep("Enter a value into the field " + sField + ":  " + sTopic);
		IRPPage.setDropdownSelection(sField, sTopic);
		
		sField = _DmsPage.sModalPrefix + "Legal Case Value";
		String sCaseNameValue = Helper.todayString() + " Case Name";
		Helper.logTestStep("Enter a value into the field " + sField + ":  " + sCaseNameValue);
		IRPPage.setTextForField(sField, sCaseNameValue);

		Helper.logTestStep("Click Create.");
		Helper.waitForThenClick(IRPPage.btnCaseNameCreate);
		
		String sExpectedSnackbar = "Created " + sCaseNameValue;
		IRPPage.verifySnackbarMessage(sExpectedSnackbar);
		
		Helper.logTestStep("Verify the Case Name was populated as expected:  " + sCaseNameValue); 
		Helper.compareLists(Arrays.asList(sCaseNameValue), RegistryAgreementPage.lsExistingSelectionsForField("Case Name"));

		Helper.logTestStep("Verify the Page Title was populated as expected:  " + sCaseNameValue); 
		if (Helper.compareStrings(sCaseNameValue, RegistryAgreementPage.getTextForField("Page Title"))) {
			Helper.logTestStep("Save the content item as draft:  " + sCaseNameValue); 
			IRPPage.saveDraft();
			
			Helper.bDebug = true;
			
			Helper.logTestStep("Set the Status to Active.");
			IRPPage.setDropdownSelection("Legal Case Status", "Active");
			
			
			Helper.logTestStep("Link (upload) a file.");
			Helper.logMessage("Click the Link File button.");
			Helper.waitForThenClick(IRPPage.btnLinkFile);
			
			Helper.logTestStep("Link (upload) a file.");
			
		}
		

	}

	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
