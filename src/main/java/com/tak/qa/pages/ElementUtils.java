package com.tak.qa.pages;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementUtils {

	private WebDriver driver;

	public ElementUtils(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/**
	 * Wait for the element to be visible
	 * 
	 * @param element
	 */
	public void waitForElementVisible(WebElement element) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Wait for an element to present
	 * 
	 * @param element
	 */
	public void waitForElement(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		wait.until(ExpectedConditions.elementToBeClickable(element));

	}

	public void waitForPageLoad() {

		long endTime = System.currentTimeMillis() + Constants.DEFAULT_TIME_OUT;

		while (System.currentTimeMillis() < endTime) {

			JavascriptExecutor js = (JavascriptExecutor) driver;
			String state = js.executeScript("return document.readyState").toString();
			System.out.println("page is : " + state);
			if (state.equals("complete")) {
				System.out.println("page is fully loaded now....");
				break;
			}

		}

	}

}
