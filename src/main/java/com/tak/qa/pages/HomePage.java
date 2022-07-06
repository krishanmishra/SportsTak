package com.tak.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

	WebDriver driver;
	private ElementUtils eleUtil;

	@FindBy(xpath = "//*[name()='path' and contains(@d,'M3 18h18v-')]")
	WebElement clickOnPath;

	@FindBy(xpath = "//p[normalize-space()='Change Language']")
	WebElement changelanguage;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public String selectLanguage() {
		
//		eleUtil.waitForPageLoad();

		eleUtil.waitForElement(clickOnPath);
		clickOnPath.click();

		eleUtil.waitForElement(changelanguage);
		changelanguage.click();

		return driver.getCurrentUrl();

	}

}
