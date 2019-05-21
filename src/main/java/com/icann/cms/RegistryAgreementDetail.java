package com.icann.cms;

import org.openqa.selenium.*;
import com.icann.Helper;

public class RegistryAgreementDetail {
	static WebDriver browser = Helper.browser;

	public static By txtAgreementTitle = By.cssSelector(".agreement__title");
	public static By txtULabel = By.cssSelector(".agreement__label");
	public static By txtOperator = By.cssSelector(".agreement__operator");
	public static By txtAgreementDate = By.cssSelector(".agreement__date");
	public static By txtAgreementType = By.cssSelector(".type");
}