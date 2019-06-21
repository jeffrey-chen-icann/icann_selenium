package com.icann.dms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.*;
import com.icann.Helper;
import com.icann.dms.contenttype.IRPPage;
import com.icann.dms.contenttype.PublicCommentPage;

public class _DmsContentItem extends _DmsPage {
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
    
    public static By btnLinkFile = Helper.anythingWithText("Link File");
    public static By btnLinkPage = Helper.anythingWithText("Link Page");
    public static By btnAddText = Helper.anythingWithText("Add Text");
    
    //upload + search for file
    public static By txtUploadFileHidden = By.xpath("//*[text()[contains(.,\"Upload files\")]]/ancestor::*[@class[contains(.,\"alfresco-link-header\")]]//input");
    public static By btnFileSearchSearchFiles = Helper.anythingWithText("Search files");
    public static By btnFileSearchApply = Helper.anythingWithText("Apply");
    public static By btnFileSearchLinkForFilename(String sFilename) {
    	return By.xpath("//*[text()=\"" + sFilename + "\"]//ancestor::*[@class[contains(.,\"adf-datatable-row\")]]//*[text()[contains(.,\"Link\")]]");
    }
    
    public static By snackbarMessage = By.xpath("//simple-snack-bar");
    
    public static String currentNodeId() {
		Helper.waitForUrlToContain("nodeId");
    	
    	String sNodeId = "unset";
    	String sCurrentUrl = browser.getCurrentUrl();
    	
    	Helper.logDebug("sCurrentUrl:  " + sCurrentUrl);
    	
    	sNodeId = sCurrentUrl.substring(sCurrentUrl.indexOf("nodeId=") + 7);
    	
    	return sNodeId;
    }
    
    public static void publish() {
    	//defaults to today - later add argument for when to publish (?)
		Helper.logMessage("Click the Publish button.");
		Helper.waitForThenClick(btnPublish);
		
		Helper.logMessage("Click the date picker.");
		Helper.waitForThenClick(PublishModal.btnDatePicker);
	
		Helper.logMessage("Click today's date.");
		Helper.waitForThenClick(PublishModal.btnDatePickerToday);
	
		Helper.logMessage("Click Submit.");
		Helper.waitForThenClick(PublishModal.btnSubmit);
		
		//when does it get published if you specify a date?
    }

    private static String sSnackbarPublishedSuccessfullyMessage = "Published successfully.";
    public static void publishNow() {
    	//defaults to today - later add argument for when to publish (?)
		Helper.logMessage("Click the More actions button.");
		Helper.waitForThenClick(btnMoreActions);
		
		Helper.logMessage("Click Publish Now.");
		Helper.waitForThenClick(btnPublishNow);
		
		Helper.logMessage("Click Publish button on confirmation modal.");
		Helper.waitForThenClick(PublishModal.btnPublishConfirm);

		verifySnackbarMessage(sSnackbarPublishedSuccessfullyMessage);
		
		waitForWorkflowState("Published");
    }
    
    public static By btnUnpublishYes = Helper.anythingWithText("Yes");
    public static By btnUnpublishNo = Helper.anythingWithText("No");
    private static String sSnackbarContentSuccessfullyUnpublishedMessage = "Content successfully unpublished.";
    public static void unpublish() {
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
    		
    		verifySnackbarMessage(sSnackbarContentSuccessfullyUnpublishedMessage);
    		
    		waitForWorkflowState("Unpublished");    		
    	} else {
    		Helper.logMessage("Current workflow state is " + sCurrentState + ".  No need to unpublish.");
    	}
    }

    public static By btnDeleteConfirm = By.xpath("//mat-dialog-container//*[text()=\"Delete\"]");
    public static void delete() {
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
    
    private static String sSnackbarTranslationRequestedSuccessfullyMessage = "Translation requested successfully.";
    public static void requestTranslation(String sLanguage) {
		Helper.logMessage("Click the More actions button.");
		Helper.waitForThenClick(btnMoreActions);
		
		Helper.logMessage("Click Request Translation.");
		Helper.waitForThenClick(btnRequestTranslation);

		String sWhichField = "Languages";
		Helper.logMessage("Set the " + sWhichField + " to value:  " + sLanguage);
		setDropdownSelection(sWhichField, sLanguage);

		Helper.logMessage("Click Submit.");
		Helper.waitForThenClick(btnRequestReviewSubmit);
		
		verifySnackbarMessage(sSnackbarTranslationRequestedSuccessfullyMessage);
    }

    public static By btnRequestReviewSubmit = By.xpath("//*[text()=\"Submit\"]/ancestor::button");
    public static By btnRequestReviewCancel = By.xpath("//*[text()=\"Cancel\"]/ancestor::button");
    private static String sSnackbarSuccessfullyAddedReviewerMessage = "Successfully added reviewer.";
    public static void requestReview(String sFromReviewer) {
		Helper.logMessage("Click the More actions button.");
		Helper.waitForThenClick(btnMoreActions);
		
		Helper.logMessage("Click Request Review.");
		Helper.waitForThenClick(btnRequestReview);

		String sWhichField = "Reviewer";
		Helper.logMessage("Set the " + sWhichField + " to value:  " + sFromReviewer);
		setDropdownSelection(sWhichField, sFromReviewer);
		
		Helper.logMessage("Click Submit.");
		Helper.waitForThenClick(btnRequestReviewSubmit);
		
		verifySnackbarMessage(sSnackbarSuccessfullyAddedReviewerMessage);
    }

    private static String sSnackbarSuccessfullySavedDraftMessage = "Successfully saved draft."; 
    public static void saveDraft() {
		Helper.logMessage("Click the Save Draft button.");
		Helper.waitForThenClick(PublicCommentPage.btnSaveDraft);

		Helper.waitForUrlToContain("nodeId");
	
		verifySnackbarMessage(sSnackbarSuccessfullySavedDraftMessage);
		
		//this workflow state does not show until the page is refreshed - do we care?
		//waitForWorkflowState("Draft");
    }
    
    private static By txtWorkflowState = By.cssSelector(".workflowState-value");
    public static String currentWorkflowState() {
    	String sCurrentState = "unset";
    	
    	sCurrentState = Helper.waitForElement(txtWorkflowState).getText().strip();
    	
    	return sCurrentState;
    }
    
    public static String waitForWorkflowState(String sDesiredState, int iSecondsToWait) {
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
    public static String waitForWorkflowState(String sDesiredState) {
    	return waitForWorkflowState(sDesiredState, 60);
    }
    
    public static void verifySnackbarMessage(String sExpectedMessage) {
		Helper.logMessage("Verify snackbar message appears:  " + sExpectedMessage);
		Helper.compareStrings(sExpectedMessage, Helper.waitForElement(snackbarMessage).getText());

		Helper.logDebug("Wait for snackbar to disappear.");
		Helper.waitForElementToDisappear(snackbarMessage);
    }
    
    public static void addFileToLinkedItemsWithSearch(String sFilename) {
		Helper.logTestStep("Link to an existing file.");
		Helper.logMessage("Click the Link File button.");
		Helper.waitForThenClick(btnLinkFile);

		Helper.logMessage("Click Search files link.");
		Helper.waitForThenClick(btnFileSearchSearchFiles);
		
		Helper.logMessage("Enter path into Name field:  "  + sFilename);
		IRPPage.setTextForField(_DmsPage.sModalPrefix + "Name", sFilename);
		
		Helper.logMessage("Click Apply.");
		Helper.waitForThenClick(btnFileSearchApply);
		Helper.nap(1);
		
		Helper.logMessage("Click the link on the row with the filename.");
		Helper.waitForThenClick(btnFileSearchLinkForFilename(sFilename));
    	
    	IRPPage.verifySnackbarMessage("Successfully linked content");
    	

    }
    
    public static void addFileToLinkedItemsWithUpload(String sFilename) {
		File f = new File("data/" + sFilename);
		
		String sFullPathFilename = f.getAbsolutePath();
		Helper.logTestStep("Link (upload) a file:  " + sFullPathFilename);
		
		Helper.waitForDisabledElement(txtUploadFileHidden).sendKeys(sFullPathFilename);;
		
		IRPPage.setTextForField(_DmsPage.sModalPrefix + "Metadata Description", f.getName() + " file metadata");

    	IRPPage.verifySnackbarMessage("Successfully linked content");
    	
		Helper.waitForThenClick(btnModalSave);
    	
    	//verify row was added to linked files
    }
    
    public static void verifyItemExistsInLinkedItems(String sFilename){
    	
		Helper.logTestStep("Verify the item exists in the list of linked items:  " + sFilename);
		Helper.logMessage("Linked items:  " + lsLinkedItems().toString());
		Helper.compareStrings("true", Boolean.toString(lsLinkedItems().contains(sFilename)));
    	
    }
    
    private static String sLinkedItemsXpathRoot = "//*[@id=\"field-nested-content-items-container\"]";
    private static By linkedItems = By.xpath(sLinkedItemsXpathRoot + "//li//div/span"); //terrible
    public static List<String> lsLinkedItems() {
    	List<String> lsLinked = new ArrayList<String>();
    	List<WebElement> lwLinkedItems = browser.findElements(linkedItems);
    	Helper.logDebug("# of linked items:  " + lwLinkedItems.size());
    	for (WebElement e : lwLinkedItems) {
 
    		//remove parens inclusive
    		//from https://stackoverflow.com/questions/5636048/regular-expression-to-replace-content-between-parentheses/5636074
            String sTemp = e.getText();
            String re = "\\([^()]*\\)";
            Pattern p = Pattern.compile(re);
            Matcher m = p.matcher(sTemp);
            while (m.find()) {
            	sTemp = m.replaceAll("");
                m = p.matcher(sTemp);
            }
            
            sTemp = sTemp.strip();
            
    		lsLinked.add(sTemp);
    	}
    	
    	return lsLinked;
    } 
    
}
