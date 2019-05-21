package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class TinyMce {
    static WebDriver browser = Helper.browser;
    static JavascriptExecutor js = (JavascriptExecutor)browser;

    public static By iframe = By.xpath("//iframe[@id[contains(.,\"ifr\")]]");
    public static By btnTinyMceSave = By.xpath("//*[@class[contains(.,\"wysiwig-button\")]]//*[text()=\"Save\"]/ancestor::button");
    public static By textarea = By.xpath("//textarea");
    
    public static void enterTextIntoTinyMceAndSave(String sTextToEnter) {
    	Helper.nap(1);
    	Helper.logMessage("Entering text:  " + sTextToEnter);
    	Helper.logDebug("Running javascript to inject text:  " + sTextToEnter);
    	js.executeScript("tinyMCE.activeEditor.setContent('" + sTextToEnter + "')");
		
		Helper.logDebug("Click tinymce iframe (to get save button to enable).");
		Helper.waitForThenClick(TinyMce.iframe);		

		Helper.nap(1);
		Helper.logTestStep("Click Save on the editor.");
		Helper.waitForThenClick(TinyMce.btnTinyMceSave);
    }
    
    public static String getTextFromTinyMce() {
    	String sMceText = Helper.waitForDisabledElement(textarea).getAttribute("value");
		
    	Helper.logDebug("textarea value:  " + sMceText);
    	
    	return sMceText;
    }

}
