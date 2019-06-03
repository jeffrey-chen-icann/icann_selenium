package com.icann.dms.contenttype;

import org.openqa.selenium.*;
import com.icann.Helper;
import com.icann.dms.ContentItem;

public class AboutTheBoardPage extends ContentItem {
	static WebDriver browser = Helper.browser;
	
    public static By txtPageTitle = By.id("icn:pageTitle");
    public static By pageDescriptionExpander = By.id("field-htmlblock1-container");

}
