package com.icann;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class WebPage {
	protected WebDriver browser;
	
	public abstract String getUrl();
	
	public WebPage(WebDriver browser) {
		setBrowser(browser);
		PageFactory.initElements(browser, this);
	}
	
	public void open() {
		browser.get(getUrl());
	}
	
	public abstract void alreadyHere() throws MalformedURLException;
	
	public void setBrowser(WebDriver browser) {
		this.browser = browser;
	}
}
