package com.icann.dms.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;
import com.icann._contentrecordtypes.BoardMeeting;
import com.icann.dms.*;
import com.icann.dms.contenttype.BoardMeetingPage;

public class _SmokeBoardMeeting {
    static WebDriver browser; 
    
	String sContentType = "Board Meeting";
    String sPageTitle = Helper.todayString() + "_boardmeetings_dms_automation";
    String sPageDate = Helper.todayString("dd MMM yyyy");
    String sMetadataDescription = sPageTitle + " metadata description";
    String sLegalDocumentYear = "2029";
    String sLegalDocumentType = "Landing Page";
    
	@BeforeClass
	static public void runOnce() throws Exception {
		browser = Environment.initializeDriver();
		Environment.setEnvironmentAndLogServers();	
		
		Helper.logTestGroup("Board Meetings tests");
		
		Dms.login();
	}

	@Before
	public void beforeEach(){
		
	}

//not used currently
	private void verifyBoardMeetingDefaults() {
		Helper.logTest("dms.boardmeeting.defaultvalues (ITI-3484) - verify default values of fields when creating a new board meeting item.");
		
		String sContentType = "Board Meeting";
		Helper.logTestStep("Create Board Meeting.");
		LandingPage.createContent(sContentType);
				
		Helper.logTestStep("Verify default page title:  " + BoardMeetingPage.sDefaultPageTitle);
		Helper.compareStrings(BoardMeetingPage.sDefaultPageTitle, Helper.waitForElement(BoardMeetingPage.txtPageTitle).getAttribute("value"));
		
		Helper.logTestStep("Verify the list of board meeting types:  " + BoardMeetingPage.lsAllBoardMeetingTypes.toString());
		Helper.compareLists(BoardMeetingPage.lsAllBoardMeetingTypes, BoardMeetingPage.lsDropdownSelections("Board Meeting Type"));
		
		//probably need more here.  default metadata?
		
	}
	
	@Test
	public void createBoardMeetingSmoke() {
		Helper.logTest("dms.smoke.boardmeeting.create (ITI-XXXX)");
			
		Helper.logTestStep("Create Content:  " + sContentType);
		LandingPage.createContent(sContentType);

//		verifyBoardMeetingDefaults();
		
		String sBoardMeetingType = "Regular";
		BoardMeeting bmRecord = new BoardMeeting(sPageTitle, sPageDate, sBoardMeetingType);
		
		Helper.logTestStep("Enter in Page Title:  " + bmRecord.sPageTitle);
		Helper.waitForThenSendKeys(BoardMeetingPage.txtPageTitle, bmRecord.sPageTitle, true);
		
		Helper.logTestStep("Enter in Page Date:  " + bmRecord.sPageDate);
		Helper.waitForThenSendKeys(BoardMeetingPage.txtPageDate, bmRecord.sPageDate);

		Helper.logTestStep("Enter in Board Meeting Type:  " + bmRecord.sBoardMeetingType);
		BoardMeetingPage.setDropdownSelection("Board Meeting Type", bmRecord.sBoardMeetingType);

		Helper.logTestStep("Enter in a Metadata Description:  " + sMetadataDescription);
		BoardMeetingPage.setTextForField("Metadata Description", sMetadataDescription);
		
//		Metadata.setMetadataDescription(sMetadataDescription);
		
		Helper.logTestStep("Enter in a Legal Document Type:  " + sLegalDocumentType);
		BoardMeetingPage.setDropdownSelection("Legal Document Type", sLegalDocumentType);
		
		Helper.logTestStep("Enter in a Legal Document Year:  " + sLegalDocumentYear);
		BoardMeetingPage.setDropdownSelection("Legal Document Year", sLegalDocumentYear);
		
		

		
		
		//save draft
		BoardMeetingPage.saveDraft();
		
		
		
		System.exit(3);
		
		//create section
		//select how to join
		//select section date
		//click ok
		//verify section bar appears
		//click section bar
		//enter some text
		//save
		
		//attach file
		//save
		
		//add document
		//select document type board meetings agenda
		//click ok
		//verify agenda page is returned
		//set title
		//set metadata
		//type dsecription text
		//save draft
		//click "back" (upper left)
		//verify back to board meeting page
		//verify new document is shown w expected publish status (draft)
		
		//publish parent, etc
		
		
	}
	


	@After
	public void afterEach() {
	}
	
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
