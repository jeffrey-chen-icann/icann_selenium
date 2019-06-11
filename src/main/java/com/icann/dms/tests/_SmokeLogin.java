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

public class _SmokeLogin {
    static WebDriver browser; 
	
	
	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers();	
		
		Helper.logTestGroup("Login smoke tests");
	}

	@Before
	public void beforeEach(){
		
	}

	@Test
	public void loginWithAdmin() {
		Helper.logTest("dms.smoke.login.admin (ITI-3446) - Log in with an admin user and verify that the admin actions are available.");
		
		Helper.logTestStep("Log in with an admin user:  " + Environment.sDmsAdminUsername());
		Dms.login(Environment.sDmsAdminUsername(), Environment.sDmsAdminPassword());
		
		Helper.logTestStep("Verify the expected header items are showing.");
		List<String> lsItemsExpected = Arrays.asList("Create Content", "Edit Existing Content", "My Tasks", "Resources", "Admin Menu");
		List<WebElement> lwItems = browser.findElements(By.xpath("//*[@class[contains(.,\"topnav__section\")]]//*[@class[contains(.,\"menu__item--label menu__item--default\")]]"));
		List<String> lsItemsActual = new ArrayList<String>();
		for (WebElement menuitem : lwItems) {
			String sText = menuitem.getText().strip();
			
			//special case because of the badge
			if (sText.contains("My Tasks")) { 
				sText = "My Tasks";
			}
			lsItemsActual.add(sText);
		}
		
		if (Helper.waitForElement(_DmsHeader.btnAdminMenu).isDisplayed()) {
			lsItemsActual.add("Admin Menu");
		}
		Helper.compareLists(lsItemsExpected, lsItemsActual, "List of admin header links.");
		
		Helper.logTestStep("Click Admin Actions dropdown.");
		Helper.waitForThenClick(_DmsHeader.btnAdminMenu);
		Helper.nap(1);
		
		Helper.logTestStep("Verify the admin menu items are as expected.");
		List<String> lsMenuItemsExpected = Arrays.asList("Create Author", "Authors", "Board Members", "Global Actions", "Managed Vocabularies");
		List<WebElement> lwMenuItems = browser.findElements(By.xpath("//button[@class[contains(.,\"mat-menu-item\")]]"));
		List<String> lsMenuItemsActual = new ArrayList<String>();
		for (WebElement menuitem : lwMenuItems) {
			lsMenuItemsActual.add(menuitem.getText().strip());
		}
		Helper.compareLists(lsMenuItemsExpected, lsMenuItemsActual, "List of admin menu submenu items.");
	}
	
	@Test
	public void loginWithNonAdmin() {
		Helper.logTest("dms.smoke.login.nonadmin (ITI-3447) - Log in with a non-admin user and verify the header items.");
		
		Helper.logTestStep("Log in with a non-admin user:  " + Environment.sDmsNonAdminUsername());
		Dms.login(Environment.sDmsNonAdminUsername(), Environment.sDmsNonAdminPassword());
		
		Helper.logTestStep("Verify the expected header items are showing.");
		List<String> lsItemsExpected = Arrays.asList("Create Content", "Edit Existing Content", "My Tasks", "Resources");
		List<WebElement> lwItems = browser.findElements(By.xpath("//*[@class[contains(.,\"topnav__section\")]]//*[@class[contains(.,\"menu__item--label menu__item--default\")]]"));
		List<String> lsItemsActual = new ArrayList<String>();
		for (WebElement menuitem : lwItems) {
			lsItemsActual.add(menuitem.getText().strip());
		}
		Helper.compareLists(lsItemsExpected, lsItemsActual, "List of non-admin header links.");
		
		Helper.logTestStep("Verify the admin actions link is not present.");
		try {
			browser.findElement(_DmsHeader.btnAdminMenu);
			Helper.logError("The admin actions link was found!  This should not be present with a non-admin user.");
		} catch (Exception e) {
			Helper.logMessage("The admin actions link was not found (as expected).");
		}
	}

	@Test
	public void loginWithInvalidUsernamePassword() {
		Helper.logTest("dms.smoke.login.invalid (ITI-3448) - Attempt to log in with an invalid username/password combo.");
		String sBogusUsername = "kratos";
		String sBogusPassword = "i hate ares";
		
		if (!browser.getCurrentUrl().contains("login")) { 
			Helper.logTestStep("Open:  " + Environment.sDmsUrl());
			browser.navigate().to(Environment.sDmsUrl());
		}
		
		Helper.logTestStep("Fill out login:  " + sBogusUsername + " / " + sBogusPassword);
		com.icann.dms.Login.fillOutLogin(sBogusUsername, sBogusPassword);
		
    	Helper.logTestStep("Click the Sign In button.");
        Helper.waitForThenClick(com.icann.dms.Login.btnSignInButton);
		
		Helper.logTestStep("Verify an error message is shown:  " + com.icann.dms.Login.sUnknownUsernameOrPasswordMessage);
		Helper.compareStrings(com.icann.dms.Login.sUnknownUsernameOrPasswordMessage, Helper.waitForElement(com.icann.dms.Login.txtErrorMessage).getText());
		
		Helper.logTestStep("Verify we are still on the login page.");
		Helper.waitForUrlToContain("login");
		Helper.waitForElement(com.icann.dms.Login.txtUsername);
		Helper.waitForElement(com.icann.dms.Login.txtPassword);
	}

	@After
	public void afterEach() {
		Helper.logMessage("");
		Helper.logMessage("Resetting to test case home state.");
		browser.navigate().refresh();
		
		if (!browser.getCurrentUrl().contains("login")) {
			Helper.logTestStep("Log out.");
			Dms.logout();
		}
		Helper.logMessage("");
	}
	
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
