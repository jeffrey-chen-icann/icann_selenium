package com.icann.dms.tests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;
import com.icann.dms.*;
import com.icann.dms.ContentType.*;

public class PublicComment {
    static WebDriver browser; 
    String sNodeId = "unset";
	String sWhichField = "";
	String sValueToSelect = "";
    
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
	
//	@Ignore
	@Test
	public void createPublicComment() {
		String sContentType = "Public Comment";
		Helper.logTest("Create content:  " + sContentType);
		
		Helper.logTestStep("Create a new item of type:  " + sContentType);
		LandingPage.createContent(sContentType);
		
		Helper.waitForUrlToContain("public-comment");
		Helper.nap(2);
	
		Helper.logTestStep("Click the Save Draft button.");
		Helper.waitForThenClick(PublicCommentPage.btnSaveDraft);
		Helper.waitForUrlToContain("nodeId");
		
		Helper.logTestStep("Verify a nodeId is returned.");
		sNodeId = PublicCommentPage.currentNodeId();
		Helper.logMessage("The created nodeId:  " + sNodeId);
		
		String sPublicCommentTitle = "Public Comment Title";
		String sPageDescription = sContentType + " page description!";
		Helper.logTestStep("Type in a public comment title:  " + sPublicCommentTitle);
		Helper.waitForThenClick(PublicCommentPage.txtPageTitle);
		Helper.waitForThenSendKeys(PublicCommentPage.txtPageTitle, sPublicCommentTitle);
		
		Helper.logTestStep("Expand the Page Text by clicking on the expander.");
		Helper.waitForThenClick(PublicCommentPage.pageDescriptionExpander);
		
		Helper.logTestStep("Type text into editor (and click Save):  " + sPageDescription);
		TinyMce.enterTextIntoTinyMceAndSave(sPageDescription);

		//hacky - setting bottom fields first so the scrollbar blocking problem (ITI-3297) is not there
		String sMetadataDescription = "metadata description text";
		Helper.logTestStep("Enter text into the Metadata Description:  " + sMetadataDescription);
		Helper.waitForThenSendKeys(Dms.txtMetadataDescription, sMetadataDescription);

		
		sWhichField = "topic owner";
		sValueToSelect = "Communications";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		Dms.setDropdownSelection(sWhichField, sValueToSelect);
		

		sWhichField = "team";
		sValueToSelect = "Language Services";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		Dms.setDropdownSelection(sWhichField, sValueToSelect);

		sWhichField = "topic";
		sValueToSelect = "Translation, Interpretation, and Localization";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		Dms.setDropdownSelection(sWhichField, sValueToSelect);
				
		Helper.logTestStep("Publish the content item now.");
		ContentItem.publishNow();
	}
	
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
