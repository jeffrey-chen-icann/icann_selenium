package com.icann.dms.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;

import com.icann.dms.*;

public class _RemoveAllAutomationData {
    static WebDriver browser; 

    String sStringToDeleteContent = "_automation";
    
	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers("dev");	
		
		Helper.logTestGroup("Delete content generated via automation.");
		
		Dms.login();		
	}

	@Before
	public void beforeEach(){
		
	}
	
	@Test
	public void test() {
		//still need to handle
		//* more than one page of results
		//* sanity check afterwards, reload search result page and filter and verify no rows show
		//* add command line parameter in launcher
		
		Helper.logMessage("Will attempt to remove any DMS content with this string in the title:  " + sStringToDeleteContent);
		EditExistingContent.open();
		Helper.logMessage("");
		
		Helper.logMessage("Type in _automation into the Page Title filter field.");
		Helper.waitForThenSendKeys(Dms.txtPageTitle, sStringToDeleteContent);
		
		Helper.logMessage("Click Apply.");
		Helper.waitForThenClick(EditExistingContent.btnApplyFilters);

		Helper.logMessage("Total results after filtering:  " + EditExistingContent.iTotalResultRows());
		Helper.logMessage("Max items per page of results:  " + EditExistingContent.iMaxItemsShownPerPage());
		
		//remember original browser tab
		String sOriginalWindowHandle = browser.getWindowHandle();
		
		//loop through result row edit buttons
		List<String> lsRows = EditExistingContent.lsAllPageTitles();
		Helper.logMessage("All titles after filtering: " + lsRows.toString());
		Helper.logMessage("");

		
		for (String sTitle : lsRows) {
			String sNewWindowHandle = "unset";
			String sTitleOnContentPage = "unset";
			Helper.logMessage("");
			Helper.logTestStep("Remove page title content:  " + sTitle);
			
			//for each edit button, click it
			Helper.logMessage("Sanity check:  does the filtered title contain the desired string?");
			if (sTitle.indexOf(sStringToDeleteContent)>=0) {
				Helper.logMessage("Pass...the filtered title (" + sTitle + ") contained the desired string (" + sStringToDeleteContent + ".");
				Helper.logTestStep("Clicking edit link for:  " + sTitle);
				Helper.waitForThenClick(EditExistingContent.btnEditContentLink(sTitle));
	
				//switch to new tab
				ArrayList<String> allTabs = new ArrayList<String>(browser.getWindowHandles());
				
				if (allTabs.size() > 1) {
					allTabs.remove(sOriginalWindowHandle);
					sNewWindowHandle = allTabs.get(0);
					Helper.logDebug("New tab detected...switching to new tab.");
					browser.switchTo().window(sNewWindowHandle);
				}
				
				Helper.waitForUrlToContain("nodeId");
				
				try {
					//verify the title contains your generic string to delete
					sTitleOnContentPage = sTitle.substring(1).substring(0, sTitle.indexOf(" Registry Agreement"));
					Helper.waitForElement(Helper.anythingWithText(sTitleOnContentPage)).isDisplayed();  //this will cause element not found exception and bail
					
					//delete the item
					Helper.logTestStep("Delete the content item:  " + sTitleOnContentPage);
					ContentItem.delete();
					
					//close tab (?)_\
					Helper.logMessage("Closing content item tab.");
					browser.close();
					
					//switch back to original tab 
					Helper.logMessage("Switching back to original tab.");
					browser.switchTo().window(sOriginalWindowHandle);
					
				} catch (Exception e) {
					Helper.logFatal("The gTLD (" + sTitleOnContentPage + ") was not found anywhere on this page!  Aborting run.  Page we were looking at:  " + browser.getCurrentUrl());
					Helper.logError(e.toString());
					
					System.exit(1);
				}
			} else {
				Helper.logFatal("The filtered title (" + sTitle + ") did not contain the desired string (" + sStringToDeleteContent + ")!  Aborting run.");
			}
		}
	}

	@AfterClass
	static public void cleanup(){
//		Helper.thatsThat();
	}
}
