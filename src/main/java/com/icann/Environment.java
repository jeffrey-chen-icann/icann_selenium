package com.icann;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
//import org.openqa.selenium.Proxy;
//import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

//import com.browserstack.local.Local;


public class Environment{
	static WebDriver browser; 
	static List<WebDriver> lwDriver = new ArrayList<WebDriver>();
	
	public static String sEnvironment = System.getenv("java_e2e_env");
	public static boolean bUseBrowserStack = false;
	public static Process bsLocalProcess = null;

	public static void setEnvironmentAndLogServers(String sEnv){
		Environment.sEnvironment = sEnv;
		
		Helper.logMessage("");
		Helper.logMessage("Environment.sEnvironment = " + Environment.sEnvironment);
				
		switch(Launcher.sProject){
		case "adf":
		case "cms":	
			//grab build tags and log them
			Helper.logMessage(">>>placeholder for project build tags:  " + Launcher.sProject);
			
			break;
		default:
			Helper.logMessage("Environment.setEnvironmentAndLogServers():  You need to put a case for your project (" + Launcher.sProject + ") in here to log your product versions in the results log.");
		}
	}
	public static void setEnvironmentAndLogServers(){
		String sDefaultEnvironment = "dev";
		setEnvironmentAndLogServers(sDefaultEnvironment);
	}
	
	static public WebDriver newBrowser() throws MalformedURLException{		
		WebDriver newBrowser = null;
		//for cbt parallel sessions
//		int iRequireMinSessions = 10;
//		int iNapPeriod = 30;
//		int iMaxTries = 30; 
		
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		
	    caps.setCapability("name", "icann " + Launcher.sCommandLineArgs);  //sets session name
//		    caps.setPlatform(Platform.YOSEMITE);  //sets OS
	    caps.setCapability("platform",  "Mac OSX 10.14");
//		    caps.setCapability("screenResolution", "1600x1200"); //desktop resolution
	    caps.setCapability("version", "latest"); //can use "latest-1", etc
	    
	    ChromeOptions options = new ChromeOptions();
	    Map<String, Object> prefs = new HashMap<String, Object>();
	    options.addArguments("disable-extensions");
	    options.addArguments("disable-browser-side-navigation");  //to try to address chromium bug with chrome 64
	    if (sEnvironment.equals("localhost")){
	    	options.addArguments("allow-running-insecure-content");
	    }
	    prefs.put("credentials_enable_service", false);
	    prefs.put("profile.password_manager_enabled", false);
	    options.setExperimentalOption("prefs", prefs);
	    caps.setCapability(ChromeOptions.CAPABILITY, options);
		caps.setCapability("record_video", "true");
		caps.setCapability("max_duration", 1800);	
		
		if (bUseBrowserStack){
			String sBSUser = "jeffchen14";
			String sBSAccessKey = "x5krsghFaunC5oXBpRpY";  //find in browserstack user settings
			String sBSUrl = String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", sBSUser, sBSAccessKey);

			String sBSTestId = "jc_selenium";
		
			try {
				//!! dependency on browserstacklocal from https://www.browserstack.com/local-testing
				String commandLine = String.format("./BrowserStackLocal --key %s --local-identifier %s", sBSAccessKey, sBSTestId);
				Helper.logDebug(String.format("Starting local process:  %s",  commandLine));
				bsLocalProcess = Runtime.getRuntime().exec(commandLine);
				
				Helper.logDebug("Waiting for browserstacklocal to be ready...input stream:");
				BufferedReader in = new BufferedReader(new InputStreamReader(bsLocalProcess.getInputStream()));
			    String line;
			    String exitCondition = "You can now access your local server(s) in our remote browser.";
			    while ((line = in.readLine()) != null) {
			        System.out.println(line);
			        if (line.contains(exitCondition)) {
			        	Helper.logDebug("Found exit condition!");
			        	break;
			        } else {
			        	Helper.nap(1);
			        }
			    }		         
			} catch (IOException e) {
				e.printStackTrace();
			}			
			
			DesiredCapabilities bsCaps = new DesiredCapabilities();
		 
			bsCaps.setCapability("name", "icann " + Launcher.sCommandLineArgs);
			bsCaps.setCapability("os", "OS X");
			bsCaps.setCapability("os_version", "Mojave");
			bsCaps.setCapability("browser", "Chrome");
			bsCaps.setCapability("browser_version", "75.0");
			bsCaps.setCapability("resolution", "1280x1024");
			bsCaps.setCapability("browserstack.local", "true");
			bsCaps.setCapability("browserstack.localIdentifier", sBSTestId);
//			caps.setCapability("browserstack.video", "false");
			
			Helper.logDebug(String.format("capabilities:  %s", bsCaps.toString()));
			
			Helper.logDebug("Attempting to get new RemoteWebDriver...");
			newBrowser = new RemoteWebDriver(new URL(sBSUrl), bsCaps);			
		} else {
			//run locally
			newBrowser = new ChromeDriver();
		}
		
		if (newBrowser != null){
			lwDriver.add(newBrowser);
			
			Helper.logMessage("Setting Helper.browser to instance (" + lwDriver.size() + ").");
			Helper.browser = newBrowser;
		} 
		
		return newBrowser;
	}

	public static WebDriver initializeDriver() throws IOException {
		Helper.logMessage("Initialzing driver.");
		
		if (bUseBrowserStack){
			Helper.logMessage("Enabling BrowserStack...");
			
			Helper.browser = newBrowser();

		} else {
			String localOS = Helper.localOS();
			Helper.logDebug("local OS:  " + localOS);
			
			switch (localOS){
			case "mac":
				break;
			case "windows":
				break;
			default:
				Helper.logFatal("Unsupported OS:  " + localOS);
			}
			
			if (Environment.sEnvironment == null){
				Environment.sEnvironment = "qastg";
			}
			
		    
//		    System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
//		    System.setProperty("webdriver.chrome.verboseLogging", "true");
			
			//can i pick different binaries here based on OS?
			if (Environment.sEnvironment.equals("localhost")){
			    DesiredCapabilities caps = DesiredCapabilities.chrome();
			    
			    caps.setCapability("name", "e2e " + Launcher.sCommandLineArgs);  //sets session name
			    caps.setPlatform(Platform.YOSEMITE);  //sets OS
			    caps.setCapability("screenResolution", "1600x1200"); //desktop resolution
			    caps.setCapability("version", "latest");
			    
			    ChromeOptions options = new ChromeOptions();
			    Map<String, Object> prefs = new HashMap<String, Object>();
			    options.addArguments("disable-extensions");
			    options.addArguments("disable-browser-side-navigation");  //to try to address chromium bug with chrome 64
			    
			    //special switch for duane
			    options.addArguments("allow-running-insecure-content");
			    
			    prefs.put("credentials_enable_service", false);
			    prefs.put("profile.password_manager_enabled", false);
			    options.setExperimentalOption("prefs", prefs);
			    caps.setCapability(ChromeOptions.CAPABILITY, options);

				Helper.browser = new ChromeDriver(caps);
			} else {
				DesiredCapabilities caps = DesiredCapabilities.chrome();
			    ChromeOptions options = new ChromeOptions();
			    options.addArguments("disable-extensions");
//			    options.addArguments("disable-browser-side-navigation");  //to try to address chromium bug with chrome 64

//			    Proxy proxy = new Proxy();
//			    proxy.setNoProxy("");
//				caps.setCapability(CapabilityType.PROXY, proxy);
				
			    caps.setCapability(ChromeOptions.CAPABILITY, options);

			    Helper.browser = new ChromeDriver(caps);
			}

		}
		
		browser = Helper.browser;  //so circular

		try{
			Dimension newSize = new Dimension(1280,1024);
			browser.manage().window().setSize(newSize);
		} catch(Exception e){
			
		}
		
		//add the new browser to the list of drivers
		lwDriver.add(browser);
		
		return browser;
	}


	
//	public static void enableSaucelabs() throws MalformedURLException {	
//		Helper.logMessage("Enabling saucelabs.");
//		
//		bUseSaucelabs = true;
//		Helper.browser = newBrowser();
//		Helper.iDefaultTimeoutInSeconds = Helper.iDefaultTimeoutInSeconds * 2;
//
//	}
//	
//	public static void enableCbt() throws MalformedURLException {	
//		Helper.logMessage("Enabling CBT.");
//				
//		bUseCbt = true;
//		Helper.browser = newBrowser();
//		Helper.iDefaultTimeoutInSeconds = Helper.iDefaultTimeoutInSeconds * 2;
//	}
//	
//	public static void markCbtSessionPassFail(boolean bPass) throws IOException {
	    // from https://help.crossbrowsertesting.com/selenium-testing/tutorials/updating-selenium-tests-pass-fail/
//		//		curl --user USER:AUTHKEY \
//		//		-X PUT -d "action=set_score" -d "score=pass" \
//		//		http://app.crossbrowsertesting.com/api/v3/selenium/TESTID
//
//		String sPassOrFail = "unset";
//		
//		if (bPass) {
//			sPassOrFail = "pass";
//		} else {
//			sPassOrFail = "fail";
//		}
//		
//		String sRequestUrl = "http://app.crossbrowsertesting.com/api/v3/selenium/" + sCBTSessionId;
//	    String[] command = {"curl", "--user", Environment.sCBTUser + ":" + Environment.sCBTAuthkey, "-X", "PUT", "-d", "action=set_score", "-d", "score=" + sPassOrFail, sRequestUrl};
//	    ProcessBuilder process = new ProcessBuilder(command); 
//        Process p;
//        
//        Helper.logMessage("Marking CBT session as " + sPassOrFail + "ed:  " + sCBTSessionId);
//        
//        try
//        {
//        	p = process.start();
//            BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
//            StringBuilder builder = new StringBuilder();
//            String line = null;
//            while ( (line = reader.readLine()) != null) {
//            	builder.append(line);
//            	builder.append(System.getProperty("line.separator"));
//            }
//            String result = builder.toString();
//            Helper.logDebug(result);
//
//        }
//        catch (IOException e)
//        {   System.out.print("error");
//            e.printStackTrace();
//        }
//
//	}
	
	
	
	//environment specific urls
	
	//ADF
	public static String sDmsUrl() {
		if (sEnvironment.contentEquals("prod")) {
			return "http://iti-adf.icann.org";
		} else {
			return String.format("http://iti-adf-%s.icann.org", sEnvironment);
		}
	}
	public static String sDmsAdminUsername(){
		String sReturn = "unset";
		
		switch (Environment.sEnvironment){
		case "localhost":
			sReturn = "";
			break;
		case "dev":
		case "staging":
		case "prod":
			sReturn = "admin@app.activiti.com";
			break;
		default:
			Helper.logFatal("sAdfAdminUsername:  sEnvironment is not set to any expected value:  " + Environment.sEnvironment);
		}
		
		return sReturn;
	}
	public static String sDmsAdminPassword(){
		String sReturn = "unset";
		
		switch (Environment.sEnvironment){
		case "localhost":
			sReturn = "";
			break;
		case "dev":
		case "staging":
		case "prod":
			sReturn = "admin";
			break;
		default:
			Helper.logFatal("sAdfAdminUsername:  sEnvironment is not set to any expected value:  " + Environment.sEnvironment);
		}
		
		return sReturn;
	}
	public static String sDmsNonAdminUsername(){
		String sReturn = "unset";
		
		switch (Environment.sEnvironment){
		case "localhost":
			sReturn = "";
			break;
		case "dev":
		case "staging":
		case "prod":
			sReturn = "test@test.com";
			break;
		default:
			Helper.logFatal("sAdfAdminUsername:  sEnvironment is not set to any expected value:  " + Environment.sEnvironment);
		}
		
		return sReturn;
	}
	public static String sDmsNonAdminPassword(){
		String sReturn = "unset";
		
		switch (Environment.sEnvironment){
		case "localhost":
			sReturn = "";
			break;
		case "dev":
		case "staging":
		case "prod":
			sReturn = "JeffIsTheBest";
			break;
		default:
			Helper.logFatal("sAdfAdminUsername:  sEnvironment is not set to any expected value:  " + Environment.sEnvironment);
		}
		
		return sReturn;
	}

	//CMS
	public static String sCmsUrl(boolean bWithLanguage){
		String sReturn = "unset";
		String sPre = "https://alpha";
		String sNonProdInsert = "";
		String sPost = ".icann.org";
		String sLanguage = "";
		
		if (Environment.sEnvironment != "prod") {
			sNonProdInsert = "-" + Environment.sEnvironment;
		}
		
		if (bWithLanguage) {
			sLanguage = "/" + sCmsLanguageAbbreviation(); 
		}
		
		sReturn = sPre + sNonProdInsert + sPost + sLanguage;
		
		return sReturn;
	}
	public static String sCmsUrl() {
		return sCmsUrl(true);
	}
	
	public static String sCmsLanguageAbbreviation() {
		String sReturn = "unset";
		
		switch (Environment.sEnvironment){
		case "arabic":
			sReturn = "ar";
			break;
		case "chinese":
			sReturn = "zh";
			break;
		case "french":
			sReturn = "fr";
			break;
		case "russian":
			sReturn = "ru";
			break;
		case "spanish":
			sReturn = "es";
			break;
		case "english":
		default:
			sReturn = "en";
		}
		
		return sReturn;
	}
}
