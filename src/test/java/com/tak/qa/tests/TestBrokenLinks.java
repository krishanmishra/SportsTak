package com.tak.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tak.qa.pages.BaseTest;

public class TestBrokenLinks extends BaseTest {
	
	
	@Test	
	public void verifyBrokenThumbnailImage() {
		
		boolean status=homePage.checkthumbnailImage();
		Assert.assertTrue(status);
		
	}

}
