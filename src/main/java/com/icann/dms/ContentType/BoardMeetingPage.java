package com.icann.dms.contenttype;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.*;
import com.icann.Helper;
import com.icann.dms._DmsContentItem;

public class BoardMeetingPage extends _DmsContentItem {
	static WebDriver browser = Helper.browser;
	
    public static By txtPageTitle = By.id("icn:pageTitle");
    public static By txtPageDate = By.id("icn:pageDate");
    public static By txtBoardMeetingType = By.id("icn:legalBoardMeetingType");
    
    public static String sDefaultPageTitle = "(Meeting Type) Meeting of the ICANN Board";

    public static List<String> lsAllBoardMeetingTypes = Arrays.asList("Informational Call", "Organizational", "Regular", "Special", "Workshop");
}
