package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;
import com.icann.dms.contenttype.RegistryAgreementPage;

public class Metadata {
    static WebDriver browser = Helper.browser;
    
    public static By txtMetadataDescription = By.id("icn:metadataDescription"); 
    
    public static void setAgreementRound(String sMetadataAgreementRound) {
    	String sWhichField = "Agreement Round";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sMetadataAgreementRound);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sMetadataAgreementRound);
    }
    public static void setAgreementStatus(String sMetadataAgreementStatus) {
    	String sWhichField = "Agreement Status";
		Helper.logTestStep("Set the " + sWhichField + " to value:  " + sMetadataAgreementStatus);
		RegistryAgreementPage.setDropdownSelection(sWhichField, sMetadataAgreementStatus);
    }

    public static void setMetadataDescription(String sMetadataDescription) {
		Helper.logTestStep("Enter text into the Metadata Description:  " + sMetadataDescription);
		Helper.waitForThenSendKeys(Metadata.txtMetadataDescription, sMetadataDescription);
    }
}
