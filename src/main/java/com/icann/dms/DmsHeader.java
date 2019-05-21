package com.icann.dms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class DmsHeader {
    static WebDriver browser = Helper.browser;
    
    public static By btnLandingPageLink = By.cssSelector(".adf-toolbar-title");
    public static By btnCreateContent = By.xpath("//app-topnav//*[@title=\"Create Content\"]");
    public static By btnEditExistingContent = By.xpath("//app-topnav//*[@title=\"Edit Existing Content\"]");
    public static By btnMyTasks = By.xpath("//app-topnav//*[@title=\"My Tasks\"]");
    public static By btnResources = By.xpath("//app-topnav//*[@title=\"Resources\"]");
    public static By btnAdminMenu = By.xpath("//app-topnav//*[@title=\"Admin Menu\"]");

    public static By mnuCreateAuthor = Helper.anythingWithText("Create Author");
    public static By mnuAuthors = Helper.anythingWithText("Authors");
    public static By mnuBoardMembers = Helper.anythingWithText("Board Members");
    public static By mnuGlobalActions = Helper.anythingWithText("Global Actions");
    public static By mnuManagedVocabularies = Helper.anythingWithText("Managed Vocabularies");
    
    public static By txtSearchField = By.id("adf-control-input");
    
}
