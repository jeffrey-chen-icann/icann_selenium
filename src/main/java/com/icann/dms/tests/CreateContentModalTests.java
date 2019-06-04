package com.icann.dms.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.icann.Environment;
import com.icann.Helper;
import com.icann.dms.*;

public class CreateContentModalTests {
    static WebDriver browser; 
	
    By contentTypeDropdownItems = By.xpath("//mat-option");
    
	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers();	
		
		Helper.logTestGroup("Create Content modal tests");
	}

	@Before
	public void beforeEach(){
		
	}


	@Test
	public void adminCreateContentDropdown() {
		Helper.logTest("dms.landingpage.createcontenttypes_admin (ITI-3468) - Log in with an admin user and verify content types in create content modal.");
		List<String> lsCreateModalItemsExpected = Arrays.asList("About the Board", "About the DIDP", "Accountability Landing", "Announcement", "Annual Reports on Accountability Mechanisms Landing", "Articles of Incorporation", "Blog", "Board Meeting Materials", "Board Meetings Agenda", "Board Meetings Minutes", "Board Meetings Preliminary Report", "Board Meetings Approved Resolutions", "Board Committee", "Board Committee Agenda", "Board Committee Minutes", "Board Governance", "Board Governance Guidelines", "Board of Directors", "Board of Directors Landing", "Board Training Material", "Bylaws", "Code of Conduct", "Complaints Officer", "Conflicts of Interest", "Corporate Compliance", "DIDP", "DIDP Landing", "File", "GDD", "Generic Page", "Governance", "Independent Review Process", "IRP Landing", "Legal Single", "Legal Structured", "Litigation Landing", "Litigation Case", "Public Comment", "Public Comment Proceedings Summary Report", "Reconsideration", "Reconsideration Landing", "Reconsideration Request", "Summary of Statements of Interest", "Base Agreement", "Related Materials", "Registry Agreement");
		List<String> lsCreateModalItemsActual = new ArrayList<String>();
		
		Helper.logTestStep("Log in with an admin user:  " + Environment.sDmsAdminUsername());
		Dms.login(Environment.sDmsAdminUsername(), Environment.sDmsAdminPassword());

		Helper.logTestStep("Click the Create Content link.");
		Helper.waitForThenClick(_DmsHeader.btnCreateContent);
		
		Helper.logTestStep("Verify the content type list is as expected.");
		Helper.waitForNumberOfElementsToAppear(contentTypeDropdownItems, 1);
		List<WebElement> lwContentTypeActual = browser.findElements(contentTypeDropdownItems);
		for (WebElement e : lwContentTypeActual) {
			lsCreateModalItemsActual.add(e.getText());
		}
		Helper.compareLists(lsCreateModalItemsExpected, lsCreateModalItemsActual);
	}
	
	@Test
	public void nonadminCreateContentDropdown() {
		Helper.logTest("dms.landingpage.createcontenttypes_nonadmin (ITI-3469) - Log in with a nonadmin user and verify content types in create content modal.");
		List<String> lsCreateModalItemsExpected = Arrays.asList("Blog", "File", "Generic Page", "Public Comment", "Public Comment Proceedings Summary Report");
		List<String> lsCreateModalItemsActual = new ArrayList<String>();
		
		Helper.logTestStep("Log in with a nonadmin user:  " + Environment.sDmsAdminUsername());
		Dms.login(Environment.sDmsNonAdminUsername(), Environment.sDmsNonAdminPassword());

		Helper.logTestStep("Click the Create Content link.");
		Helper.waitForThenClick(_DmsHeader.btnCreateContent);
		
		Helper.logTestStep("Verify the content type list is as expected.");
		Helper.waitForNumberOfElementsToAppear(contentTypeDropdownItems, 1);
		List<WebElement> lwContentTypeActual = browser.findElements(contentTypeDropdownItems);
		for (WebElement e : lwContentTypeActual) {
			lsCreateModalItemsActual.add(e.getText());
		}
		Helper.compareLists(lsCreateModalItemsExpected, lsCreateModalItemsActual);
	}

	@After
	public void afterEach() {
		Helper.logTestStep("Clean up.");
		Helper.waitForThenClick(CreateContentModal.btnCancel);		
		Dms.logout();
	}
	
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
