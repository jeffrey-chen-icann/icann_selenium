package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class _DmsHeader {
    static WebDriver browser = Helper.browser;
    
    public static By btnLandingPageLink = By.cssSelector(".adf-toolbar-title");
    public static By btnCreateContent = By.xpath("//app-topnav//*[@title=\"Create Content\"]");
    public static By btnEditExistingContent = By.xpath("//app-topnav//*[@title=\"Edit Existing Content\"]");
    public static By btnMyTasks = By.xpath("//app-topnav//*[@title=\"My Tasks\"]");
    public static By btnResources = By.xpath("//app-topnav//*[@title=\"Resources\"]");
    public static By btnAdminMenu = By.xpath("//app-topnav//*[@title=\"Admin Menu\"]");

    public static By mnuAdminCreateAuthor = Helper.anythingWithText("Create Author");
    public static By mnuAdminAuthors = Helper.anythingWithText("Authors");
    public static By mnuAdminBoardMembers = Helper.anythingWithText("Board Members");
    public static By mnuAdminGlobalActions = Helper.anythingWithText("Global Actions");
    public static By mnuAdminManagedVocabularies = Helper.anythingWithText("Managed Vocabularies");
    
    public static By txtSearchField = By.id("adf-control-input");
    
    public static By btnUserDropdown = By.xpath("//*[@class[contains(.,\"current-user__full-name\")]]");
    public static By mnuLogout = Helper.anythingWithText("Sign out");
    
	public static void logout(){
		Helper.logMessage("Click the user dropdown link.");
		Helper.waitForThenClick(btnUserDropdown);
		
		Helper.logMessage("Click the Sign out link.");
		Helper.waitForThenClick(mnuLogout);
		
		Helper.logMessage("Verify the login page is returned.");
		Helper.waitForUrlToContain("login");
		Helper.waitForElement(Login.txtUsername);
	}
}
