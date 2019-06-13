package com.icann.dms;

import java.util.ArrayList;

import org.openqa.selenium.*;

import com.icann.Environment;
import com.icann.Helper;

public class LandingPage extends _DmsPage {
    static WebDriver browser = Helper.browser;
    
    //buttons in the center of the page
    public static By btnCreateContent = By.xpath("//app-landing-page//*[@title=\"Create Content\"]");
    public static By btnEditExistingContent = By.xpath("//app-landing-page//*[@title=\"Edit Existing Content\"]");
    public static By btnMyTasks = By.xpath("//app-landing-page//*[@title=\"My Tasks\"]");
    public static By btnResources = By.xpath("//app-landing-page//*[@title=\"Resources\"]");
    
    public static void open() {
    	Helper.logTestStep("Opening landing page.");
    	browser.navigate().to(Environment.sDmsUrl() + "/landing-page");
    }
    
    public static String createContent(String sContentType) {
		String sWindowHandle = browser.getWindowHandle();
		String sPreType = "unset";
		String sSub = "";
		String sExpectToSeeInUrl = "unset";
		
		switch (sContentType) {
		
		case "Board Meetings":
			sExpectToSeeInUrl = "board-meetings";
			sPreType = "Board Meeting Materials"; 
			break;
		case "Board Committee Meetings":
			sExpectToSeeInUrl = "board-committee-meetings";
			sPreType = "Board Meeting Materials"; 
			break;
		case "Secretary's Notice":
			sExpectToSeeInUrl = "secretarys-notice";
			sPreType = "Board Meeting Materials"; 
			break;
		case "Registry Agreement":
			sExpectToSeeInUrl = "registry-agreement";
			break;
		default:
			Helper.logError("You need a case for your content type here:  LandingPage.createContent()");
		}
		
		Helper.logMessage("Click the Create Content button.");
		Helper.waitForThenClick(btnCreateContent);
		
		if (sPreType.equals("unset")) {
			//do nothing special
		} else {
			sSub = "child ";
			Helper.logMessage("Choose a content type:  " + sPreType);
			Helper.waitForThenClick(Helper.anythingWithText(sPreType));
		}
		
		Helper.logMessage("Choose a " + sSub + "content type:  " + sContentType);
		Helper.waitForThenClick(Helper.anythingWithText(sContentType));
		
		Helper.logMessage("Click the Create button.");
		Helper.waitForThenClick(CreateContentModal.btnCreate);
		
		Helper.nap(1);
		
		ArrayList<String> allTabs = new ArrayList<String>(browser.getWindowHandles());
		
		if (allTabs.size() > 1) {
			Helper.logMessage("*** New tab detected...switching to new tab and closing old tab.");
			browser.close();
			allTabs.remove(sWindowHandle);
			browser.switchTo().window(allTabs.get(0));
		}
		
		Helper.waitForUrlToContain(sExpectToSeeInUrl);
		Helper.nap(2);
		
		return sWindowHandle;
    }
}
