package com.tak.qa.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

	public HomePage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//*[name()='path' and contains(@d,'M3 18h18v-')]")
	WebElement clickOnPath;

	@FindBy(xpath = "//p[normalize-space()='Change Language']")
	WebElement changelanguage;
	
	@FindBy(tagName = "img")
	List<WebElement> images;
	
	@FindBy(xpath = "//span[normalize-space()='All']")
	WebElement bgcolor;

	@FindBy(xpath = "//p[normalize-space()='Theme']")
	WebElement theme;
	
	public String selectLanguage() {
		
		navigateToUrl("https://thesportstak.com/");
		clickOn(clickOnPath);

		clickOn(changelanguage);

		return driver.getCurrentUrl();

	}

	public boolean checkthumbnailImage() {
		
		navigateToUrl("https://thesportstak.com/");
		
		waitForPageLoaded();
		
		List<String> images=findAllLinksOnPage();
		System.out.println("Total no. of images are " + images.size());
		for(String imageurl:images) {
//			clickOn(imageurl);
			System.out.println(imageurl);
			return isResponseForLinkTwoHundredOrThreeOTwo(imageurl);
		}
		return false;
	}

	public boolean changeTheme() {
		
		navigateToUrl("https://thesportstak.com/");
		waitForPageLoaded();
		
		String bgHex=ColorVerify(bgcolor,"border-bottom-color");
		System.out.println(bgHex);
		
		clickOn(clickOnPath);
		
		clickOn(theme);
		waitForPageLoaded();
		
		String bgcolor2=ColorVerify(bgcolor,"border-bottom-color");
		System.out.println(bgcolor2);
		
		if(!(bgHex.equals(bgcolor2))) {
			return true;
		}else {
			return false;
		}
		
		
	}

}
