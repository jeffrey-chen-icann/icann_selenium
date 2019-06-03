package com.icann.dms.tests;

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;
import com.icann.dms.*;

public class Login_AccessAdminPagesWithNonAdmin {
    static WebDriver browser; 
	
	
	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers();	
		
		Helper.logTestGroup("Login tests");
	}

	@Before
	public void beforeEach(){
		
	}


	@Test
	public void loginWithNonAdminAndAccessAdminPages() {
		Helper.logTest("dms.login.nonadmin.accessadminpages (ITI-3451) - Log in with a non-admin user and verify admin pages cannot be accessed.");
		List<String> lsAdminUrls = Arrays.asList(
				Environment.sDmsUrl() + "/content/author",
				Environment.sDmsUrl() + "/edit-authors",
				Environment.sDmsUrl() + "/edit-board-members",
				Environment.sDmsUrl() + "/bulk",
				Environment.sDmsUrl() + "/managed-vocabularies"
				);
		
		Helper.logTestStep("Log in with a non-admin user:  " + Environment.sDmsNonAdminUsername());
		Dms.login(Environment.sDmsNonAdminUsername(), Environment.sDmsNonAdminPassword());
		
		
		for (String sUrl : lsAdminUrls) {
			Helper.logTestStep("Navigate directly to the admin page:  " + sUrl);
			browser.navigate().to(sUrl);
			Helper.nap(2);
						
			Helper.logTestStep("Verify the page shows an error message:  " + Dms.sPermissionsError);
			try {
				browser.findElement(Dms.txtPermissionsError);
				Helper.logMessage("The error message was found (as expected).");
				
			} catch (Exception e) {
				Helper.logError("The error message was not found when accessing this page with a non-admin user:  " + sUrl);
				Helper.logKnownFailure("https://jira.icann.org/browse/ITI-3435", "non-admin can access admin-only pages");
			}
			Helper.logMessage("");
			Helper.logMessage("");
		}
	}

	@After
	public void afterEach() {

	}
	
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
