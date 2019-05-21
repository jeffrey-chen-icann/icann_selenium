package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class PublishModal {
    static WebDriver browser = Helper.browser;
    
    private static String sBasePublishModalXpath = "//app-submit-for-publish";
    public static By txtDate = By.id("pageDate");
    public static By btnDatePicker = By.xpath(sBasePublishModalXpath + "//mat-datepicker-toggle/button");
    public static By btnSubmit = By.xpath(sBasePublishModalXpath + "//*[text()=\"Submit\"]");
    public static By btnCancel = Helper.anythingWithText("Cancel");
    
    public static By btnDatePickerToday = By.xpath("//*[@class[contains(.,\"mat-calendar-body-today\")]]");
    
    public static By btnPublishConfirm = By.xpath("//aca-confirm-delete//span[text()=\"Publish\"]//ancestor::button");

}
