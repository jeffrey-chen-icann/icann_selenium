package com.icann.cms;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.*;

import com.icann.Environment;
import com.icann.Helper;
import com.icann._contentrecordtypes.RegistryAgreement;

public class RegistryAgreementsPage {
	static WebDriver browser = Helper.browser;

	public static By btnApplyFilters = By.xpath("//button[text()=\"Apply\"]");
	
	public static void open() {
		String sUrl = Environment.sCmsUrl() + "/registry-agreements"; 
		Helper.logMessage("Opening:  " + sUrl);
		browser.navigate().to(sUrl);
		
		Helper.nap(2);
	}
	
	// enter-select type dropdowns - might move to a more generic class at some point
	private static String sEnterSelectRootXpath(String sWhichField) {
		String sReturn = "unset"; 
		switch (sWhichField) {
		case "Content Type":  //content type on the create content modal
			sReturn = "//*[text()[contains(.,\"" + sWhichField + "\")]]/ancestor::mat-form-field";
			break;
		default:
			sReturn = "//*[text()[contains(.,\"" + sWhichField + "\")]]/ancestor::iti-form-field";			
		}
		
		return sReturn;
	}

	public static By txtEnterSelect(String sWhichField) {
		return By.xpath(sEnterSelectRootXpath(sWhichField) + "//input");
	}

	public static By txtEnterSelectSearch(String sWhichField) {
		return By.xpath(
				"(" + sEnterSelectRootXpath(sWhichField) + "//*[@class[contains(.,\"search-drop-down\")]])[1]/input");
	}

	private static String sEnterSelectSelectionRootXPath(String sWhichField) {
		return sEnterSelectRootXpath(sWhichField) + "//div[@class[contains(.,\"search-drop-down\")]]//label";
	}

	private static List<String> lsEnterSelectSearchSelections(String sWhichField) {
		List<String> lsReturn = new ArrayList<>();
		String sSelectionXpath = sEnterSelectSelectionRootXPath(sWhichField) + "/span";
		By txtSelection = By.xpath(sSelectionXpath);
		int iChoices = browser.findElements(txtSelection).size();
		boolean bThisMightTakeAWhile = false;
		
		Helper.logMessage("# of choices in dropdown " + sWhichField + " = " + iChoices);
		
		if (iChoices > 100) {
			bThisMightTakeAWhile = true;
			Helper.logMessage("This might take a while...");
		}
		for (int i = 0; i < iChoices; i++) {
			if (bThisMightTakeAWhile && i%100==0) {
				Helper.logMessage(".");
			}
			String sChoiceText = browser.findElement(By.xpath("(" + sSelectionXpath + ")[" + (i+1) + "]")).getText();
			Helper.logDebug(sChoiceText);
			lsReturn.add(sChoiceText);
		}

		return lsReturn;
	}
	
	public static List<String> lsSelectionsForField(String sWhichField) {
		List<String> lsSelections; 
		
		Helper.logDebug("Click the " + sWhichField + " text field.");
		Helper.waitForThenClick(RegistryAgreementsPage.txtEnterSelect(sWhichField));
		Helper.nap(2);
		
		lsSelections = lsEnterSelectSearchSelections(sWhichField);
		
		Helper.logDebug("Click the " + sWhichField + " text field (to collapse the dropdown).");
		Helper.waitForThenClick(RegistryAgreementsPage.txtEnterSelect(sWhichField));
		
		return lsSelections;
	}
	
	public static By chkFilterSelection(String sFilterItemText) {
		return By.xpath("//*[text()=\"" + sFilterItemText + "\"]/ancestor::*[@class[contains(.,\"search-drop-down__item\")]]//input");
	}
	
	
	
	
	//results grid
	private static String sSearchResultsBaseXpath = "//iti-ra-search-results";
	
	public static By lResultRows = By.xpath(sSearchResultsBaseXpath + "//tr[@class=\"show-for-large\"]");
	
	public static By lSingleResultRowFields(int iWhichRow) {
		return By.xpath("(" + sSearchResultsBaseXpath + "//tr[@class=\"show-for-large\"])[" + iWhichRow + "]/td");
	}
	
	public static RegistryAgreement searchResultRowValue(int iWhichRow) {
		RegistryAgreement raReturn = new RegistryAgreement();
		
		Helper.waitForElement(lSingleResultRowFields(iWhichRow));
		
		List<WebElement> lsFieldValuesForOneRow = browser.findElements(lSingleResultRowFields(iWhichRow)); 
		int iNumOfFields = lsFieldValuesForOneRow.size();
		
		if (iNumOfFields == 5) {
			raReturn.sGtld = lsFieldValuesForOneRow.get(0).getText();
			raReturn.sAgreementType = lsFieldValuesForOneRow.get(1).getText();
			raReturn.sOperator = lsFieldValuesForOneRow.get(2).getText();
			raReturn.sAgreementStatus = lsFieldValuesForOneRow.get(3).getText();
			raReturn.sAgreementDate = lsFieldValuesForOneRow.get(4).getText();
		} else {
			Helper.logFatal("Unexpected number of fields shown on registry agreements search result table:  " + iNumOfFields);
		}
		
		return raReturn;
	}
	
	public static int iCurrentNumberOfResultRowsShown() {
		Helper.waitForNumberOfElementsToAppear(RegistryAgreementsPage.lResultRows, 1);
		
		List<WebElement> lsResults = browser.findElements(RegistryAgreementsPage.lResultRows);
		int iNumOfResults = lsResults.size();
		
		Helper.logDebug("Number of result rows:  " + iNumOfResults);
		return iNumOfResults;
	}

	public static By resultLink(String sWhichGtld) {
		return By.xpath("//a[text()=\"" + sWhichGtld + "\"]");
	}
}
