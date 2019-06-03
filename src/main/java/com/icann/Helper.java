package com.icann;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class Helper {
	public static WebDriver browser; 
    public static boolean bDebug = false;
    public static boolean bLogging = true;
	public static int iDefaultTimeoutInSeconds = 10;
	private static int iDefaultLogLevel = 1;
	
	public static boolean bIgnoreEnabledDisplayed = false;
	
	private static List<String> lsRecoverableErrors = new ArrayList<String>();
	
	public static String randomHexColor(){
		String sHexColor;
		
		Color cColor = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
		sHexColor = String.format("#%02X%02X%02X", cColor.getRed(), cColor.getGreen(), cColor.getBlue());
		
		return sHexColor;
	}
	
	public static WebElement waitForElement(By byLocator, int iTimeoutInSeconds) {
		//be aware of local class variable:  bIgnoreEnabledDisplayed
		WebElement eElement = null;
		boolean bElementFound = false;
		boolean bEnabled = false;
		boolean bDisplayed = false;
		
		logDebug("Looking for element " + byLocator.toString() + ".");
		
		for(int i=0; i<iTimeoutInSeconds; i++){
			if (!browser.findElements(byLocator).isEmpty()){				
				bElementFound = true;
				if (bIgnoreEnabledDisplayed) {
					eElement = browser.findElement(byLocator);
				}
				else {
					for(int j=0; j<5; i++, j++){ //now wait until it's enabled and displayed
						bEnabled = browser.findElement(byLocator).isEnabled();
						bDisplayed = browser.findElement(byLocator).isDisplayed();
						if ((bEnabled && bDisplayed) || bIgnoreEnabledDisplayed){
							String sEnabledDisplayed = "";
							if (bEnabled && bDisplayed){
								sEnabledDisplayed = "enabled/displayed ";
							}
							logDebug("Found " + sEnabledDisplayed + "element " + byLocator.toString() + " after " + i + " seconds.");
							nap(1);
							eElement = browser.findElement(byLocator);
							break;
						}
						else {
							logDebug("Found element " + byLocator.toString() + ", but waiting for it to become enabled/displayed...");
							Helper.logDebug("bEnabled = " + bEnabled + "; bDisplayed = " + bDisplayed);
							nap(1);
						}
					}
					break;
				}
			}
			else
			{
				nap(1);
			}
		}
		if (eElement==null){
			if (bElementFound){
				Helper.logMessage("The element was found, but it never became displayed/enabled after " + iTimeoutInSeconds + " second(s):  " + byLocator);
			} else {
				Helper.logError("Never found the element after " + iTimeoutInSeconds + " second(s):  " + byLocator);
			}
		}
		
		return eElement;
	}
	public static WebElement waitForElement(By byLocator){ 
		return waitForElement(byLocator, iDefaultTimeoutInSeconds); 
	}
	
	public static WebElement waitForDisabledElement(By byLocator, int iTimeoutInSeconds){
		WebElement eElement = null;
		boolean bElementFound = false;

		for(int i=0; i<iTimeoutInSeconds; i++){
			if (!browser.findElements(byLocator).isEmpty()){				
				bElementFound = true;
				eElement = browser.findElement(byLocator);
			} else {
				nap(1);
			}
		}
		
		if (!bElementFound) {
			Helper.logError("Never found the element after " + iTimeoutInSeconds + " second(s):  " + byLocator);
		}
		
		return eElement;
	}
	
	public static WebElement waitForDisabledElement(By byLocator){ 
		return waitForDisabledElement(byLocator, iDefaultTimeoutInSeconds); 
	}

	
	public static void waitForThenClick(By byLocator, int iTimeoutInSeconds){
		Helper.logDebug("Clicking:  " + byLocator);
		waitForElement(byLocator, iTimeoutInSeconds).click();				
	}
	public static void waitForThenClick(By byLocator){
		waitForElement(byLocator, iDefaultTimeoutInSeconds).click();
	}
	
	public static void waitForHoverThenClick(By byHoverLocator, By byClickLocator){
		Actions aCtion = new Actions(browser);
		
		Helper.logDebug("Hovering over:  " + byHoverLocator);
		aCtion.moveToElement(waitForElement(byHoverLocator)).perform();
		waitForThenClick(byClickLocator);		
	}
	
	public static void waitForThenSendKeys(By byLocator, String sTextToType, boolean bClearFieldFirst){
		if (bClearFieldFirst){
			Helper.logDebug("Clearing field.");
			waitForElement(byLocator, iDefaultTimeoutInSeconds).clear();
		}
		Helper.logDebug("Sending keys:  " + sTextToType);
		waitForElement(byLocator, iDefaultTimeoutInSeconds).sendKeys(sTextToType);
	}
	
	public static void waitForThenSendKeys(By byLocator, String sTextToType){
		waitForThenSendKeys(byLocator, sTextToType, false);
	}
	
	public static String waitForUrlToContain(String sLookForText, int iTimeoutInSeconds) {
		//returns what the URL changed to
		
		Helper.logDebug("Waiting for URL to contain:  " + sLookForText);
		
		String sUrlChangedTo = "";
		nap(1);
		
		for(int i=1; i<iTimeoutInSeconds; i++){
			sUrlChangedTo = browser.getCurrentUrl();
			Helper.logDebug("Current url:  " + sUrlChangedTo);
			if (sUrlChangedTo.indexOf(sLookForText)>-1){
				Helper.logDebug("Found string '" + sLookForText + "' in url after " + i + " seconds.");
				break;
			}
			else {
				nap(1);
			}
		}
		
		if (sUrlChangedTo.indexOf(sLookForText)==-1){
			Helper.logMessage("After " + iTimeoutInSeconds + " seconds, the URL never changed to include '" + sLookForText + "'.  Current URL:  " + sUrlChangedTo);
		}
		return sUrlChangedTo;
	}
	public static String waitForUrlToContain(String sLookForText) {
		return waitForUrlToContain(sLookForText, iDefaultTimeoutInSeconds);
	}
	
	public static void nap(int iTimeoutInSeconds){
		for(int i=0; i<iTimeoutInSeconds; i++){
			try{
				Helper.logDebug(".");
				Thread.sleep(1000);
			}
			catch (Exception e){
			}
		}
	}
	
	public static boolean waitForNumberOfElementsToAppear(By byWhatXpath, int iNumberOfElementsRequired, boolean bThrowErrorIfNotEnough){
		//use this if you need for > 1 items to have appeared before continuing (like a dropdown option list)
		//if you only need one, use waitForElement
		
		boolean bFoundSome = false;
		int iDefaultTimeout = 10;
		int i;
		int iNumberOfElementsShowing = 0;

		Helper.logDebug("Looking for " + iNumberOfElementsRequired + " elements using xpath:  " + byWhatXpath);
		
		for (i=0; i<iDefaultTimeout; i++){
			iNumberOfElementsShowing = browser.findElements(byWhatXpath).size();
			
			if (iNumberOfElementsShowing >= iNumberOfElementsRequired){
				Helper.logDebug(iNumberOfElementsShowing + " element(s) found for xpath:  " + byWhatXpath);
				bFoundSome = true;
				break;
			} else {
				Helper.logDebug("Elements found:  " + iNumberOfElementsShowing + " (waiting for " + iNumberOfElementsRequired + ")");
				Helper.nap(1);
			}
		}
		
		if (!bFoundSome) {
			if (bThrowErrorIfNotEnough){
				Helper.logError("Never found enough elements (wanted  " + iNumberOfElementsRequired + "; found " + iNumberOfElementsShowing + ") using xpath:  " + byWhatXpath + " after " + iDefaultTimeout + " seconds.");
			} else {
				Helper.logDebug("Found " + iNumberOfElementsShowing + " element(s) found for xpath:  " + byWhatXpath);
			}
			
		}
		
		return bFoundSome;
	}
	public static boolean waitForNumberOfElementsToAppear(By byWhatXpath, int iNumberOfElementsRequired){
		return waitForNumberOfElementsToAppear(byWhatXpath, iNumberOfElementsRequired, true);
	}
	
	public static void waitForElementToDisappear(By byLocator, int iTimeoutInSeconds){
		boolean bElementGone = false;
		int i;
		
		Helper.logDebug("Waiting for element to disappear:  " + byLocator);
		//wait for element to not be there
		for (i=0; i<iTimeoutInSeconds; i++){
			if (browser.findElements(byLocator).size() == 0){
				bElementGone = true;
				Helper.logDebug("Element was not present!");
				break;
			} else {
				Helper.nap(1);
			}
		}
		
		if (bElementGone){
			if (i==0){
				Helper.logDebug("Element never existed or disappeared immediately:  " + byLocator);
			} else {
				Helper.logMessage("Element disappeared after " + i + " seconds:  " + byLocator);
			}
		} else {
			fail("Element still existed after " + iTimeoutInSeconds + " seconds:  " + byLocator);
		}
		
		Helper.nap(2);
	}
	public static void waitForElementToDisappear(By byLocator){
		waitForElementToDisappear(byLocator, iDefaultTimeoutInSeconds);
	}

	public static void waitForElementTextToEqual(By byLocator, String sExpectedText, int iWaitUpToSeconds){
        logDebug("Waiting up to " + iWaitUpToSeconds + " seconds for " + byLocator + ".getText() to equal \"" + sExpectedText + "\".");
        
		for (int i=0; i<10; i++){
        	if (waitForElement(byLocator).getText().equals(sExpectedText)){
        		break;
        	}
        }
	}
	public static void waitForElementTextToEqual(By byLocator, String sTextToContain){
		waitForElementTextToEqual(byLocator, sTextToContain, iDefaultTimeoutInSeconds);
	}
	
	public static boolean waitForNumberOfWindows(int iNumberOfWindowsToWaitFor){
		boolean bFoundEnough = false;
		int iSecondsToWait = Helper.iDefaultTimeoutInSeconds;
		int i;
		
		for (i=0; i<iSecondsToWait || bFoundEnough; i++){
			if (browser.getWindowHandles().size() >= iNumberOfWindowsToWaitFor){
				Helper.logDebug("Found " + browser.getWindowHandles().size() + " windows.");
				break;
			} else {
				nap(1);
			}	
		}
		return bFoundEnough;
	}
	
	public static By anythingWithText(String sTextToLookFor){ 
		
		String sXpath = "//*[text()=\"" + sTextToLookFor + "\" or text()[contains(.,\"" + sTextToLookFor + "\")]]";

		if (sTextToLookFor.contains("\"")){
			Helper.logDebug("Fourd double quotes in string to search for, so replacing double quotes in contains xpath call with single quotes.");
			sXpath = "//*[text()[contains(.,'" + sTextToLookFor + "')]]";
		}
		return By.xpath(sXpath);
	}
	
	public static void logMessage(String sMessage, int iLogLevel){
		String sOneTab = "   ";
		String sTabs = "";
		
		if (iLogLevel>0){
			iLogLevel++;
		}
		
		for (int i = 0; i < iLogLevel; i++){
			sTabs += sOneTab;
		}
		
		if (bLogging){
			System.out.println(sTabs + sMessage);
		}
	}
	public static void logMessage(String sMessage){
		logMessage(sMessage, iDefaultLogLevel);
	}
	public static void logDebug(String sMessage){
		if (bDebug) {
			logMessage("DEBUG> " + sMessage, 0);
		}
	}
	
	public static void logTestGroup(String sTestGroupDescription){
		String sBorder = "################################################################################";
		
		logMessage("", 0);
		logMessage("", 0);
		logMessage(sBorder, 0);
		logMessage("# " + sTestGroupDescription, 0);
		logMessage(sBorder, 0);
		logMessage("", 0);
	}
	
	public static void logTest(String sTestDescription){
		String sBorder = "########################################";

		logMessage("", 0);
		logMessage("", 0);
		logMessage(sBorder, 0);
		logMessage(sTestDescription, 0);
		logMessage("", 0);
	}
	
	public static void logTestStep(String sTestStep){
		String sBeginStep = "# ";
		
		logMessage(sBeginStep + sTestStep, 0);
	}
	
	public static void logError(String sErrorText){
		String sPreText = "ERROR> ";
		
		logMessage(sPreText + sErrorText, 0);		
		lsRecoverableErrors.add(sErrorText);
	}
	
	public static void logKnownFailure(String sIssueLink, String sDescription){
		String sPreText = "KNOWN BUG>>>>";
		String sMessageToLog = sPreText + sDescription + " " + sIssueLink;
		logMessage(sPreText, 0);
		logMessage(sMessageToLog, 0);		
		logMessage(sPreText, 0);
		lsRecoverableErrors.add(sMessageToLog);
	}
	
	public static void logFatal(String sReason) {
		String sPreText = "FATAL> ";
		
		logMessage("", 0);				
		logMessage("***", 0);
		logMessage(sPreText + sReason, 1);
		logMessage("***", 0);
		logMessage("", 0);
		
		logMessage("Aborting.");
		
		thatsThat();
	}
	
	public static String localOS () {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "windows";
		} else if (os.contains("nux") || os.contains("nix")) {
			return "linux";
		}else if (os.contains("mac")) {
			return "mac";
		}else if (os.contains("sunos")) {
			return "solaris";
		}else {
			return "other";
		}
	}
		
	public static void thatsThat() {
		boolean bRecoverableErrors = lsRecoverableErrors.size() > 0;
		
		logMessage("");
		logMessage("");
		logTest("");		
		logMessage("");
		
		if (bRecoverableErrors){
			logMessage("Non-fatal errors occurred during the test run.  See log above for details.");

			int iErrors = lsRecoverableErrors.size();
			
			for (int i=0; i<iErrors; i++){
				Helper.logMessage((i+1) + "     " + lsRecoverableErrors.get(i), 2);
			}

			logMessage("There were errors recovered from during the test run.  See log above for details.");

//			if (Environment.bUseCbt){
//				try {
//					Environment.markCbtSessionPassFail(false);
//				} catch (Exception e){
//					
//				}
//			}
//			browser.quit();
//			fail();
//			
//		} else {
//			//insert call to mark session as passed (?) << is this true?
//			if (Environment.bUseCbt){
//				try {
//					Environment.markCbtSessionPassFail(true);
//				} catch (Exception e){
//					
//				}
//			}
			

		}
		
		if (Helper.bDebug) {
			Helper.logMessage("Leaving browser open since we are in debug mode.");
		} else {
			browser.quit();
		}
		
		if (bRecoverableErrors){
			fail();
		}
	}
	
	public static boolean compareLists(List<String> lsExpected, List<String> lsActual, String sWhatAreTheseLists){
		boolean bEqual = false;
		if (!sWhatAreTheseLists.equals("")){
			logMessage("Comparing:  " + sWhatAreTheseLists);
		}
		
		if(lsActual.equals(lsExpected)){
			bEqual = true;
	    	logMessage("The lists were equal (" + lsExpected.size() + " items):  " + lsExpected);
		} else {
	    	logError("The lists were not equal:  " + sWhatAreTheseLists);
	    	logError("Expected items (" + lsExpected.size() + ") vs. actual items (" + lsActual.size() + "):");
	    	logError("Expected:  " + lsExpected);
	    	logError("Actual:    " + lsActual);
	    	
	    	Helper.logDebug("<variable_here> = Arrays.asList(" + sConvertListToStringForAsList(lsActual) + ");");
		}
		
		return bEqual;
	}	
	public static boolean compareLists(List<String> lsExpected, List<String> lsActual){
		return compareLists(lsExpected, lsActual, "");
	}
	
	private static String sConvertListToStringForAsList(List<String> lsStringsToConvert) {
		String sReturn = "";
		String sCommaIfAny = ", ";
		for (int i=0; i<lsStringsToConvert.size(); i++) {
			if (i+1 == lsStringsToConvert.size()) {
				sCommaIfAny = "";

			}
			sReturn += "\"" + lsStringsToConvert.get(i).replaceAll("\"", "\\\\\"") + "\"" + sCommaIfAny;
		}
		
		return sReturn;
	}
	
	public static boolean compareStrings(String sExpected, String sActual, String sWhatAreTheseStrings){
		boolean bEqual = false;
		if (!sWhatAreTheseStrings.equals("")){
			logMessage("Comparing:  " + sWhatAreTheseStrings);
		}
		
		if(sActual.equals(sExpected)){
			bEqual = true;
	    	logMessage("The strings were equal:  " + sExpected);
		} else {
	    	logError("The strings were not equal.");
	    	logError("Expected:  " + sExpected);
	    	logError("Actual:    " + sActual);
		}
		
		return bEqual;
	}	
	public static boolean compareStrings(String sExpected, String sActual){
		return compareStrings(sExpected, sActual, "");
	}
    
    public static WebDriver switchBackToDefaultBrowser(){
    	Helper.logMessage("Closing current browser (" + Environment.lwDriver.size() + ").");
    	Environment.lwDriver.get(Environment.lwDriver.size()-1).close();
    	Environment.lwDriver.remove(Environment.lwDriver.size()-1);
    	Helper.logMessage("Setting Helper.browser back to the default browser (1).");
    	Helper.browser = Environment.lwDriver.get(0);
    	
    	return Environment.lwDriver.get(0);
    }
    
    public static boolean elementIsEnabled(By byLocator){
    	Helper.nap(1);
    	return browser.findElement(byLocator).isEnabled();
    }
    
    public static HttpResponse apiCall(String sRequestUrl, String sRequestType, String sRequestBody) throws ClientProtocolException, IOException{
		//right now assumes if sBentoJwt not set then assemblyapi call (otherwise bento call)
		//need to modify if we need other api calls here later
    	
    	//for now only handles POST!
		
		Helper.logDebug("sRequestUrl:   " + sRequestUrl);
		Helper.logDebug("sRequestType:  " + sRequestType);
		Helper.logDebug("sRequestBody:  " + sRequestBody);
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse response;
		StringEntity entity = new StringEntity(sRequestBody, ContentType.APPLICATION_JSON); 
        HttpPost request = new HttpPost(sRequestUrl);
        request.setEntity(entity);
        
		response = httpClient.execute(request);
        
		return response;
	}
    public static HttpResponse apiDelete(String sRequestUrl) throws ClientProtocolException, IOException{
 		Helper.logMessage("sRequestUrl:   " + sRequestUrl);
 		
 		HttpClient httpClient = HttpClientBuilder.create().build();
 		HttpResponse response;
        HttpDelete request = new HttpDelete(sRequestUrl);
         
 		response = httpClient.execute(request);
 		return response;
 	}

	public static String todayString(String sDateFormat) {
		DateFormat df;
		String sTodayString;
		if (sDateFormat.equals("Month")) {
			df = new SimpleDateFormat("MMM", Locale.ENGLISH);
			sTodayString = df.format(new Date());
		} else {
			df = new SimpleDateFormat(sDateFormat);
			sTodayString = df.format(new Date());
		}
		
		return sTodayString;
	}
	public static String todayString() {
		return todayString("yyyyMMdd_HH.mm.ss");
	}
}
