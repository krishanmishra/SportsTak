package com.tak.qa.tests;

import org.testng.annotations.Test;

import com.tak.qa.pages.BaseTest;

public class TestChangeTheme extends BaseTest {

	@Test
	public void changeTheme() {
		
		 homePage.changeTheme();
		
	}

}
