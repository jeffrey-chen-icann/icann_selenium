package com.icann.dms.contenttype;

import org.openqa.selenium.*;
import com.icann.dms.ContentItem;

public class RegistryAgreementPage extends ContentItem {
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
}
