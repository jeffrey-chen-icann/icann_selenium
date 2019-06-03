package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;
import com.icann.dms.contenttype.PublicCommentPage;

public class ContentItem {
    static WebDriver browser = Helper.browser;
    
    public static By btnPreview = Helper.anythingWithText("Preview");
    public static By btnSaveDraft = Helper.anythingWithText("Save Draft");
    public static By btnPublish = Helper.anythingWithText("Publish");
    public static By btnMoreActions = Helper.anythingWithText("More actions");
    
    private static String sMoreActionsXpathRoot = "//*[@class[contains(.,\"mat-menu-content\")]]";
    public static By btnPublishNow = By.xpath(sMoreActionsXpathRoot + "//button[text()[contains(.,\"Publish Now\")]]");
    public static By btnUnpublish = By.xpath(sMoreActionsXpathRoot + "//button[text()[contains(.,\"Unpublish\")]]");
    public static By btnRequestTranslation = By.xpath(sMoreActionsXpathRoot + "//button[text()[contains(.,\"Request Translation\")]]");
    public static By btnRequestReview = By.xpath(sMoreActionsXpathRoot + "//button[text()[contains(.,\"Request Review\")]]");
    public static By btnDelete = By.xpath(sMoreActionsXpathRoot + "//button[text()[contains(.,\"Delete\")]]");
    
    public static String currentNodeId() {
		Helper.waitForUrlToContain("nodeId");
    	
    	String sNodeId = "unset";
    	String sCurrentUrl = browser.getCurrentUrl();
    	
    	Helper.logDebug("sCurrentUrl:  " + sCurrentUrl);
    	
    	sNodeId = sCurrentUrl.substring(sCurrentUrl.indexOf("nodeId=") + 7);
    	
    	return sNodeId;
    }
    

    
    static public void publish() {
    	//defaults to today - later add argument for when to publish (?)
		Helper.logMessage("Click the Publish button.");
		Helper.waitForThenClick(btnPublish);
		
		Helper.logMessage("Click the date picker.");
		Helper.waitForThenClick(PublishModal.btnDatePicker);
	
		Helper.logMessage("Click today's date.");
		Helper.waitForThenClick(PublishModal.btnDatePickerToday);
	
		Helper.logMessage("Click Submit.");
		Helper.waitForThenClick(PublishModal.btnSubmit);
		
		//detect snackbar message?
		Helper.nap(2);
    }

    static public void publishNow() {
    	//defaults to today - later add argument for when to publish (?)
		Helper.logMessage("Click the More actions button.");
		Helper.waitForThenClick(btnMoreActions);
		
		Helper.logMessage("Click Publish Now.");
		Helper.waitForThenClick(btnPublishNow);
		
		Helper.logMessage("Click Publish button on confirmation modal.");
		Helper.waitForThenClick(PublishModal.btnPublishConfirm);

		waitForWorkflowState("Published");
    }
    
    static public By btnUnpublishYes = Helper.anythingWithText("Yes");
    static public By btnUnpublishNo = Helper.anythingWithText("No");
    static public void unpublish() {
    	//only do it if we need to
    	String sCurrentState = currentWorkflowState();
    	if (sCurrentState.contentEquals("Published")) {
    		Helper.logMessage("Current workflow state is " + sCurrentState + ".  Unpublishing.");
    		Helper.logMessage("Click the More actions button.");
    		Helper.waitForThenClick(btnMoreActions);
    		
    		Helper.logMessage("Click Unpublish.");
    		Helper.waitForThenClick(btnUnpublish);

    		Helper.logMessage("Click Yes on confirmation modal.");
    		Helper.waitForThenClick(btnUnpublishYes);
    		
    		waitForWorkflowState("Unpublished");    		
    	} else {
    		Helper.logMessage("Current workflow state is " + sCurrentState + ".  No need to unpublish.");
    	}
    }

    static public By btnDeleteConfirm = By.xpath("//mat-dialog-container//*[text()=\"Delete\"]");
    static public void delete() {
    	Helper.logMessage("Deleting content with nodeId:  " + currentNodeId());
    	unpublish();
    	
		Helper.logMessage("Click the More actions button.");
		Helper.waitForThenClick(btnMoreActions);
		
		Helper.logMessage("Click Delete.");
		Helper.waitForThenClick(btnDelete);
		
		Helper.logMessage("Click Delete button on confirmation modal.");
		Helper.waitForThenClick(btnDeleteConfirm);
    	
		Helper.waitForUrlToContain("landing-page");
    }
    static public void requestTranslation(String sLanguage) {
		Helper.logMessage("Click the More actions button.");
		Helper.waitForThenClick(btnMoreActions);
		
		Helper.logMessage("Click Request Translation.");
		Helper.waitForThenClick(btnRequestTranslation);

		String sWhichField = "Languages";
		Helper.logMessage("Set the " + sWhichField + " to value:  " + sLanguage);
		Dms.setDropdownSelection(sWhichField, sLanguage);

		Helper.logMessage("Click Submit.");
		Helper.waitForThenClick(btnRequestReviewSubmit);
		
		//detect snackbar message?
		Helper.nap(2);
    }

    static public By btnRequestReviewSubmit = By.xpath("//*[text()=\"Submit\"]/ancestor::button");
    static public By btnRequestReviewCancel = By.xpath("//*[text()=\"Cancel\"]/ancestor::button");
    static public void requestReview(String sFromReviewer) {
		Helper.logMessage("Click the More actions button.");
		Helper.waitForThenClick(btnMoreActions);
		
		Helper.logMessage("Click Request Review.");
		Helper.waitForThenClick(btnRequestReview);

		String sWhichField = "Reviewer";
		Helper.logMessage("Set the " + sWhichField + " to value:  " + sFromReviewer);
		Dms.setDropdownSelection(sWhichField, sFromReviewer);
		
		Helper.logMessage("Click Submit.");
		Helper.waitForThenClick(btnRequestReviewSubmit);
		
		//detect snackbar message?
		Helper.nap(2);
    }

    static public void saveDraft() {
		Helper.logMessage("Click the Save Draft button.");
		Helper.waitForThenClick(PublicCommentPage.btnSaveDraft);

		//detect snackbar message?
		Helper.nap(2);
    }
    
    static private By txtWorkflowState = By.cssSelector(".workflowState-value");
    static public String currentWorkflowState() {
    	String sCurrentState = "unset";
    	
    	sCurrentState = Helper.waitForElement(txtWorkflowState).getText().strip();
    	
    	return sCurrentState;
    }
    
    static public String waitForWorkflowState(String sDesiredState, int iSecondsToWait) {
    	String sCurrentState = currentWorkflowState();
    	String sWorkflowStateText = "Workflow state = ";
    	boolean bMatches = false;
    	int iRetryTimeInSeconds = 5;
    	
    	Helper.logMessage("Waiting up to " + iSecondsToWait + " seconds for workflow state to change to:  " + sDesiredState);
    	sCurrentState = currentWorkflowState();
    	
    	int i;
    	for (i=0; i<iSecondsToWait/iRetryTimeInSeconds; i++) {
    		if (sCurrentState.equals(sDesiredState)) {
    			bMatches = true;
    			break;
    		} else {
    			Helper.logDebug(sWorkflowStateText + sCurrentState + "; refreshing page in " + iRetryTimeInSeconds + " seconds...");
    			Helper.nap(iRetryTimeInSeconds);
    			Helper.logDebug("Refreshing page.");
    			browser.navigate().refresh();
    			sCurrentState = currentWorkflowState();
    		}
    	}

    	if (bMatches) {
    		Helper.logMessage(sWorkflowStateText + sCurrentState + " after " + (i+1)*iRetryTimeInSeconds + " seconds.");
    	} else {
    		Helper.logError("State (" + sDesiredState + ") was not achieved after " + iSecondsToWait + " seconds.");
    	}
    	return sCurrentState;
    }
    static public String waitForWorkflowState(String sDesiredState) {
    	return waitForWorkflowState(sDesiredState, 60);
    }
}
