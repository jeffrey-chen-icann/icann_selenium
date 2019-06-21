package com.icann.dms.contenttype;

import org.openqa.selenium.*;

import com.icann.Helper;
import com.icann.dms._DmsContentItem;

public class IRPPage extends _DmsContentItem {
	public static WebDriver browser = Helper.browser;
	
    private static String sCaseNameXpathRoot = "//*[@id=\"field-icn:legalCase-container\"]";
    public static By btnManageCaseName = By.xpath(sCaseNameXpathRoot + "//*[text()=\"Manage\"]");

    
    private static String sCaseNameModalBaseXpath = "//aca-create-value-dialog";
    public static By btnCaseNameCreate = By.xpath(sCaseNameModalBaseXpath + "//*[text()[contains(.,\"Create\")]]");

}

