package com.tak.qa.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tak.qa.pages.BaseTest;
import com.tak.qa.pages.Constants;
import com.tak.qa.pages.HomePage;

public class TestLanguage extends BaseTest {

	WebDriver driver;

	HomePage homepage;
	

	@Test
	public void selectlanguage() {
		homepage = new HomePage(driver);
		String url=homepage.selectLanguage();
		Assert.assertTrue(url.contains(Constants.URL_FRACTION));
	}
	
	

}
