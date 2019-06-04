package com.icann.dms.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.icann.Environment;
import com.icann.Helper;
import com.icann.dms.*;

public class BoardMeetingsTests {
    static WebDriver browser; 
	
    
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


	@Test
	public void createBoardMeeting() {
		Helper.logTest("dms.boardmeeting.create (ITI-) - .");
		
		Helper.bDebug = true;
		
		String sContentType = "Board Meetings";
		Helper.logTestStep("Create Content:  " + sContentType);
		LandingPage.createContent(sContentType);
		
		//enter page title
		//enter page date
		//enter board meeting type
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

	}
	
	@AfterClass
	static public void cleanup(){
		Helper.thatsThat();
	}
}
