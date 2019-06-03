package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class CreateContentModal {
    static WebDriver browser = Helper.browser;
    
    public static By btnCreate = By.xpath("//aca-create-content-modal//*[text()=\"Create\"]");
    public static By btnCancel = By.xpath("//aca-create-content-modal//*[text()=\"Cancel\"]");
}
