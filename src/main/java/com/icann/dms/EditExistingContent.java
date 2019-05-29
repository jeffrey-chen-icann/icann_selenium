package com.icann.dms;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;

import com.icann.Environment;
import com.icann.Helper;

public class EditExistingContent {
    static WebDriver browser = Helper.browser;
    
    private static String sEditContentUrl = Environment.sDmsUrl() + "/search;q=*";
    
    public static void open() {
    	Helper.logMessage("Opening edit content page:  " + sEditContentUrl);
    	browser.navigate().to(sEditContentUrl);
    }
    
    //filters
    public static By btnApplyFilters = Helper.anythingWithText("Apply");
    public static By btnClearFilters = Helper.anythingWithText("Clear");
    
    
    //search results
    public static By btnEditContentLink(String sPageTitle) {
    	return By.xpath(sRowWithTitleXpath(sPageTitle) + "//*[@title=\"Edit\"]");
    }
    private static String sRowWithTitleXpath(String sPageTitle) {
    	return "//*[@title=\"" + sPageTitle + "\"]/ancestor::*[@class[contains(.,\"row\")]]";
    }
    private static By rowWithTitleData(String sPageTitle) {
    	return By.xpath(sRowWithTitleXpath(sPageTitle) + "//div[@class[contains(.,\"data-table-cell\")]]");
    }
    public static List<String> lsRowWithTitleData(String sPageTitle){
    	List<String> lsRowData = new ArrayList<String>();
    	
    	Helper.logDebug("Waiting for row to appear:  " + sRowWithTitleXpath(sPageTitle));
    	Helper.waitForElement(rowWithTitleData(sPageTitle));
    	
    	Helper.logDebug("Looping through cells to grab data.");
    	List<WebElement> lwCells = browser.findElements(rowWithTitleData(sPageTitle));
    	int iNumOfCells = lwCells.size()-1; //use -1 because the last cell is the edit link
    	for (int i=0; i<iNumOfCells; i++) {
    		String sCellText = lwCells.get(i).getText().strip();
    		lsRowData.add(sCellText);
    		Helper.logDebug(sCellText);
    	}
    	    	
    	return lsRowData;
    }
    
    private static By pageTitles = By.xpath("//*[@title=\"Page Title\"]");
    public static List<String> lsAllPageTitles() {
    	List<String> lsAllTitles = new ArrayList<String>();
    	
    	Helper.waitForElement(pageTitles);
    	List<WebElement> lwPageTitles = browser.findElements(pageTitles);
    	
    	Helper.logDebug("Getting all displayed page titles...");
    	int iNumOfTitles = lwPageTitles.size();
    	for (int i=1; i<iNumOfTitles; i++) { //skipping first one because it is the table header row
    		lsAllTitles.add(lwPageTitles.get(i).getText());
    		Helper.logDebug(lsAllTitles.get(lsAllTitles.size()-1));
    	}
    	
    	return lsAllTitles;
    }
    
    private static By searchReturned0Results = Helper.anythingWithText("Your search returned 0 results");
    private static By showingXofY = By.xpath("//span[@class=\"adf-pagination__range\"]");
    public static int iTotalResultRows() {
    	int iTotalRows = 0;
    	
    	try {
    		Helper.logDebug("Looking for pagination control...");
    		browser.findElement(showingXofY);
    		
	    	String sShowingXofY = Helper.waitForElement(showingXofY).getText();
	
	    	String sY = sShowingXofY.substring(sShowingXofY.lastIndexOf(" ")+1);
	    	iTotalRows = Integer.parseInt(sY);
    		
    	} catch (Exception e) {
    		Helper.logDebug("Never found pagination control.  Assuming 0 results.");
    	}

    	return iTotalRows;
    }
    private static By itemsPerPage = By.xpath("//*[@class=\"adf-pagination__max-items\"]");
    public static int iMaxItemsShownPerPage() {
    	return Integer.parseInt(Helper.waitForElement(itemsPerPage).getText());
    }
    private static By btnItemsPerPageDropdown = By.xpath("//*[@class[contains(.,\"adf-pagination__perpage-block\")]]//button");
    private static By btnItemsPerPage(int iMaxItemsPerPage) {
    	return By.xpath("//button[text()[contains(.,\"" + iMaxItemsPerPage + "\")]]");
    }
    public static void setItemsPerPage(int iDesiredNumberOfItems) {
    	Helper.logMessage("Clicking items per page dropdown.");
    	Helper.waitForThenClick(btnItemsPerPageDropdown);
    	
    	Helper.logMessage("Clicking item:  " + iDesiredNumberOfItems);
    	Helper.waitForThenClick(btnItemsPerPage(iDesiredNumberOfItems));
    	Helper.nap(2);
    }
}
