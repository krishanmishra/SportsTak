package com.tak.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tak.qa.pages.BaseTest;
import com.tak.qa.pages.Constants;

public class TestLanguage extends BaseTest {

	@Test
	public void selectlanguage() {
		
		String url = homePage.selectLanguage();
		Assert.assertTrue(url.contains(Constants.URL_FRACTION));
	}

}
