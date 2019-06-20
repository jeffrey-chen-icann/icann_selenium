package com.icann.dms.contenttype;

import org.openqa.selenium.*;

import com.icann.Helper;
import com.icann._contentrecordtypes.RegistryAgreement;
import com.icann.dms._DmsContentItem;

public class RegistryAgreementPage extends _DmsContentItem {
    public static By pageTextExpander = By.id("field-htmlblock1-container");
    
    private static String sGtldStringXpathRoot = "//*[@id=\"field-icn:associatedTLD-container\"]";
    public static By btnManageGtldString = By.xpath(sGtldStringXpathRoot + "//*[text()=\"Manage\"]");
    public static By txtAgreementType = By.id("icn:raType");
    
    //TLD modal
    private static String sTldModalBaseXpath = "//aca-create-tld-dialog";
    public static By txtTldALabel = By.xpath(sTldModalBaseXpath + "//*[@id=\"icn:aLabel\"]");
    public static By txtTldULabel = By.xpath(sTldModalBaseXpath + "//*[@id=\"icn:uLabel\"]");
    public static By txtTldTldTranslation = By.xpath(sTldModalBaseXpath + "//*[@id=\"icn:tldTranslation\"]");
    public static By txtTldTypeOfTld = By.xpath(sTldModalBaseXpath + "//*[@id=\"icn:typeOfTld\"]");
    public static By chkPartOfARegistryAgreement = By.xpath(sTldModalBaseXpath + "//*[@id=\"icn:isRA-input\"]/ancestor::div[1]");
    public static By btnTldCreate = By.xpath(sTldModalBaseXpath + "//*[text()[contains(.,\"Create\")]]");    
    
    public static void createGtldString(RegistryAgreement raRecord) {
		Helper.logMessage("Click Manage on the gTLD/String field.");
		Helper.waitForThenClick(btnManageGtldString);
		
		Helper.logMessage("Set the A-Label:  " + raRecord.sGtld);
		Helper.waitForThenSendKeys(txtTldALabel, raRecord.sGtld);
		
		Helper.logMessage("Set the U-Label:  " + raRecord.sULabel);
		Helper.waitForThenSendKeys(txtTldULabel, raRecord.sULabel);

		String sWhichField = "Type of TLD";
		String sValueToSelect = raRecord.sTypeOfTld;
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sValueToSelect);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sValueToSelect);

		Helper.logTestStep("Check the Part of Registry Agreement checkbox.");
		Helper.waitForThenClick(chkPartOfARegistryAgreement);
		
		Helper.logTestStep("Click the Create button.");
		Helper.waitForThenClick(btnTldCreate);
		
		verifySnackbarMessage("Successfully created the TLD: '" + raRecord.sGtld + ".'");
		
		Helper.logTestStep("Verify the gTLD field was populated as expected:  " + raRecord.sGtld); 
		Helper.compareStrings(raRecord.sGtld, RegistryAgreementPage.lsExistingSelectionsForField("gTLD/String").get(0));
    }

	public static void verifyFields(RegistryAgreement raRecord) {
		
		
	}
}
