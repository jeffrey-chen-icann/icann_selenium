package com.icann.dms.contenttype;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.*;

import com.icann.Helper;
import com.icann.dms._DmsContentItem;
import com.icann.dms._DmsPage;

public class IRPPage extends _DmsContentItem {
	public static WebDriver browser = Helper.browser;
	
    private static String sCaseNameXpathRoot = "//*[@id=\"field-icn:legalCase-container\"]";
    public static By btnManageCaseName = By.xpath(sCaseNameXpathRoot + "//*[text()=\"Manage\"]");

    
    private static String sCaseNameModalBaseXpath = "//aca-create-value-dialog";
    public static By btnCaseNameCreate = By.xpath(sCaseNameModalBaseXpath + "//*[text()[contains(.,\"Create\")]]");
    
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
    	
    	//verify row was added to linked files
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

