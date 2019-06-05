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
import com.icann.dms.contenttype.BoardMeetingsPage;

public class BoardMeetingsTests {
    static WebDriver browser; 
    
	String sContentType = "Board Meetings";
    String sPageTitle = Helper.todayString() + "_boardmeetings_dms_automation";
    String sPageDate = Helper.todayString("dd MMM yyyy");
    
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

//	@Ignore
	@Test
	public void boardMeetingDefaults() {
		Helper.logTest("dms.boardmeeting.defaultvalues (ITI-3484) - verify default values of fields when creating a new board meeting item.");
		
		String sContentType = "Board Meetings";
		Helper.logTestStep("Create Content:  " + sContentType);
		LandingPage.createContent(sContentType);
				
		Helper.logTestStep("Verify default page title:  " + BoardMeetingsPage.sDefaultPageTitle);
		Helper.compareStrings(BoardMeetingsPage.sDefaultPageTitle, Helper.waitForElement(BoardMeetingsPage.txtPageTitle).getAttribute("value"));
		
		Helper.logTestStep("Verify the list of board meeting types:  " + BoardMeetingsPage.lsAllBoardMeetingTypes.toString());
		Helper.compareLists(BoardMeetingsPage.lsAllBoardMeetingTypes, BoardMeetingsPage.lsDropdownSelections("Board Meeting Type"));
		
		//probably need more here.  default metadata?
		
	}
		@Ignore
		@Test
	public void createBoardMeetingSmoke() {
		Helper.logTest("dms.boardmeeting.defaultvalues (ITI-3484) - verify default values of fields when creating a new board meeting item.");
			
		Helper.logTestStep("Create Content:  " + sContentType);
		LandingPage.createContent(sContentType);

		String sBoardMeetingType = "Regular";
		
		BoardMeeting bmRecord = new BoardMeeting(sPageTitle, sPageDate, sBoardMeetingType);
		
		Helper.logTestStep("Enter in page title:  " + bmRecord.sPageTitle);
		Helper.waitForThenSendKeys(BoardMeetingsPage.txtPageTitle, bmRecord.sPageTitle, true);
		
		Helper.logTestStep("Enter in page date:  " + bmRecord.sPageDate);
		Helper.waitForThenSendKeys(BoardMeetingsPage.txtPageDate, bmRecord.sPageDate);

		Helper.bDebug = true;
		
		Helper.logTestStep("Enter in board meeting type:  " + bmRecord.sBoardMeetingType);
		BoardMeetingsPage.setDropdownSelection("Board Meeting Type", bmRecord.sBoardMeetingType);
		
		//enter legal document year
		
		
		//enter legal document type
		
		
		//enter metadata desc
		
		
		//save draft
		
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
		Helper.logTestStep("Clean up.");
		LandingPage.open();

	}
	
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
