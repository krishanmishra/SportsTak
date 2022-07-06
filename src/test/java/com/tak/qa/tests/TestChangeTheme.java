package com.tak.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tak.qa.pages.BaseTest;

public class TestChangeTheme extends BaseTest {

	@Test
	public void changeTheme() {
		
		 boolean status=homePage.changeTheme();
		 Assert.assertTrue(status);
		
	}

}
