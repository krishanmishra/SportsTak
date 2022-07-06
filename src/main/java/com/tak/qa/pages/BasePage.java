package com.tak.qa.pages;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;

public abstract class BasePage {
	protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);	

	protected static WebDriverWait ajaxWait;
	protected WebDriver driver;

	
	public BasePage(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Click action performed and then wait
	 * 
	 * @param element
	 */
	public void waitAndClick(WebElement element) {
		waitForElement(element);
		element.click();
		waitForPageLoaded();
	}

	/**
	 * Click on an element
	 * 
	 * @param element
	 */
	public void clickOn(WebElement element) {
		waitForElement(element);
		element.click();
	}

	/**
	 * Click on an element with Action
	 * 
	 * @param element
	 */
	public void actionClick(WebElement element) {
		waitForElement(element);
		Actions action = new Actions(driver);
		action.click(element).build().perform();
		waitForSpinner();
	}

	/**
	 * Accept an alert displayed
	 */
	public void acceptAlert() {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	/**
	 * switchToFrame by id or name
	 * 
	 * @param id
	 */
	public void switchToFrame(String name) {
		_waitForJStoLoad();
		getDriver().switchTo().frame(name);
	}

	/* Handle child windows */
	public String switchPreviewWindow() {
		Set<String> windows = getDriver().getWindowHandles();
		Iterator<String> iter = windows.iterator();
		String parent = iter.next();
		getDriver().switchTo().window(iter.next());
		return parent;
	}

	/**
	 * switchToWindow by id or name
	 * 
	 * @param id
	 * @throws InterruptedException
	 */

	public void switchToWindow(WebElement element, String text) throws InterruptedException {

		String Parent_Window = driver.getWindowHandle();

		for (String Child_Window : driver.getWindowHandles()) {
			driver.switchTo().window(Child_Window);
			Assert.assertTrue(getElementText(element).contains(text));
			logger.info("Autopop text message:" + getElementText(element));
		}
		// Switching back to Parent Window
		driver.switchTo().window(Parent_Window);

	}

	/**
	 * switchToFrame by web element
	 * 
	 * @param webelement
	 */

	public void switchToFrame(WebElement webelement) {
		_waitForJStoLoad();
		getDriver().switchTo().frame(webelement);
	}

	/**
	 * switchTo default main Content
	 */
	public void switchToMainContents() {
		_waitForJStoLoad();
		getDriver().switchTo().defaultContent();
	}

	/**
	 * Click on element by web element
	 * 
	 * @param webElement
	 */
	public void javascriptButtonClick(WebElement webElement) {
		waitForElement(webElement);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", webElement);
	}

	/**
	 * Click on element by web element
	 * 
	 * @param webElement
	 */
	public void javascriptInputText(WebElement webElement, String inputText) {
		waitForElement(webElement);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('value','" + inputText + "')", webElement);
	}

	/**
	 * Click on element by web element
	 * 
	 * @param webElement
	 */
	public void javascriptInputText(WebElement webElement, int inputText) {
		waitForElement(webElement);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('value','" + inputText + "')", webElement);
	}

	/**
	 * Click on element by string locator
	 * 
	 * @param locator
	 */
	public void waitAndClick(String locator) {
		this.WaitForElementPresent(locator, 30);
		Assert.assertTrue(isElementPresent(locator), "Element Locator :" + locator + " Not found");
		WebElement el = getDriver().findElement(ByLocator(locator));
		el.click();

	}

	/**
	 * Click on element by string locator
	 * 
	 * @param locator
	 */
	public void clickOn(String locator) {
		WebElement el = getDriver().findElement(ByLocator(locator));
		el.click();
	}

//	public String returnTitle() {
//		return title;
//	}

	/**
	 * Scroll page down 250 pixel
	 */
	public void scrollDown() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,250)", "");
	}

	/**
	 * Scroll page down pixel
	 * 
	 * @Param pixel pixel to scroll down
	 */
	public void scrollDown(String pixel) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0," + pixel + ")", "");
	}

	/**
	 * Scroll page up 250 pixel
	 */
	public void scrollUp() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(250, 0)", "");
	}

	/**
	 * Scroll page up pixel
	 * 
	 * @Param pixel pixel to scroll down
	 */
	public void scrollUp(String pixel) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(" + pixel + ", 0)", "");
	}

	/**
	 * Set implicit wait
	 * 
	 * @param timeInSec
	 */
	private void setImplicitWait(int timeInSec) {
		logger.info("setImplicitWait, timeInSec={}", timeInSec);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeInSec));
	}

	/**
	 * RFeset implicit wait
	 */
	private void resetImplicitWait() {
		logger.info("resetImplicitWait");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
	}

	/**
	 * Waiting for an expected condition
	 * 
	 * @param expectedCondition
	 */
	public void waitFor(ExpectedCondition<Boolean> expectedCondition) {
		setImplicitWait(0);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		wait.until(expectedCondition);
		resetImplicitWait();
	}

	/**
	 * Providing inputs to the given location
	 * 
	 * @param element
	 * @param text
	 */
	public void inputText(WebElement element, String text) {
		waitForElementVisible(element);
//		element.clear();
		_waitForJStoLoad();
		element.sendKeys(text);

	}

	public WebElement getWebElementById(String text) {
		return driver.findElement(By.xpath("//span[contains(@id,'" + text + "')]"));
	}

	public WebElement getWebElementByClass(String text) {
		return driver.findElement(By.xpath("//span[contains(@class,'" + text + "')]"));
	}

	/**
	 * Providing inputs to the given location
	 * 
	 * @param element
	 * @param text
	 */
	public void inputText(WebElement element, int k) {
		waitForElement(element);
		element.clear();
		_waitForJStoLoad();
		element.sendKeys(String.valueOf(k));
		_waitForJStoLoad();

	}

	/**
	 * Wait for an element to present
	 * 
	 * @param element
	 */
	public void waitForElement(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			logger.info(element.toString() + " is not present on page or not clickable");
		}

	}

	public WebElement waitForElementPresenceWithFluentWait(WebElement element, int timeout) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofMillis(1000))
				.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
				.withMessage("Element not found");

		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Wait for the element to be visible
	 * 
	 * @param element
	 */
	public void waitForElementVisible(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			logger.info(element.toString() + " is not present on page");
		}
	}

	/**
	 * Wait for the elements to be visible
	 * 
	 * @param elements
	 */
	public void waitForElementsVisible(List<WebElement> elements) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
			wait.until(ExpectedConditions.visibilityOfAllElements(elements));
		} catch (Exception e) {
			logger.info(elements.toString() + " is not present on page");
		}
	}

	/**
	 * Wait for element
	 * 
	 * @param locator
	 */
	public void waitForElement(String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		if (locator instanceof String)
			wait.until(ExpectedConditions.visibilityOfElementLocated(ByLocator(locator)));
	}

	/**
	 * Wait for element using string builder
	 * 
	 * @param stringbuilder_locator
	 * @return
	 */
	public boolean waitForElement(StringBuilder stringbuilder_locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		wait.until(ExpectedConditions.visibilityOfElementLocated(ByLocator(stringbuilder_locator)));
		return true;
	}

	/**
	 * Wait for page using java script
	 * 
	 * @return
	 */
	public boolean _waitForJStoLoad() {
		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		/**
		 * wait for JavaScript to load
		 */
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				Object rsltJs = ((JavascriptExecutor) driver).executeScript("return document.readyState");
				if (rsltJs == null) {
					rsltJs = "";
				}
				return rsltJs.toString().equals("complete") || rsltJs.toString().equals("loaded");
			}
		};

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

	/**
	 * Handle locator type
	 * 
	 * @param locator
	 * @return
	 */
	public By ByLocator(String locator) {
		By result = null;
		if (locator.startsWith("//") || locator.startsWith("(//")) {
			result = By.xpath(locator);
		} else if (locator.startsWith("css=")) {
			result = By.cssSelector(locator.replace("css=", ""));
		} else if (locator.startsWith("#")) {
			result = By.id(locator.replace("#", ""));
		} else if (locator.startsWith("name=")) {
			result = By.name(locator.replace("name=", ""));
		} else if (locator.startsWith("link=")) {
			result = By.linkText(locator.replace("link=", ""));
		} else {
			result = By.className(locator);
		}
		return result;
	}

	/**
	 * 
	 * @param locator
	 * @return
	 */
	public By ByLocator(StringBuilder locator) {
		By result = null;

		if (locator.charAt(0) == 'x') {
			result = By.xpath(locator.toString().replace("xpath=", ""));
		} else if (locator.charAt(0) == 'c') {
			result = By.cssSelector(locator.toString().replace("css=", ""));
		} else if (locator.charAt(0) == 'i') {
			result = By.id(locator.toString().replace("#", ""));
		}
		return result;
	}

	/**
	 * 
	 * @param lettersNum
	 * @return
	 */
	public static String generateRandomNumber(int lettersNum) {
		String finalString = "9";
		int letter;
		for (int i = 0; i < lettersNum - 1; i++) {
			letter = generateRandomNumber(0, 9);
			finalString += String.valueOf(letter);
		}
		return finalString;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public boolean verifyURL(String url) {
		boolean value = false;
		String currentUrl = driver.getCurrentUrl();
		if (currentUrl.contains(url))
			return true;
		else
			return value;
	}

	/**
	 * 
	 * @param URL
	 */
	public void navigateToUrl(String URL) {
		waitForPageLoaded();
		driver.get(URL);
	}

	public WebDriver getDriver() {
		return driver;
	}

//	public void assertByPageTitle() {
//		try {
//			if (driver instanceof ChromeDriver || driver instanceof InternetExplorerDriver
//					|| driver instanceof FirefoxDriver) {
//				Thread.sleep(3000);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Assert.assertTrue(returnTitle().equals(driver.getTitle()));
//	}

	public List<String> findAllLinksOnPage() {
		List<String> links = new ArrayList<String>();
		List<WebElement> linkElements = driver.findElements(By.tagName("img"));
		for (WebElement each : linkElements) {
			String link = each.getAttribute("src");
			if (link == null || link.contains("mailto") || link.contains("javascript")) {
				continue;
			}
			links.add(link);
		}
		return links;
	}

	public boolean isResponseForLinkTwoHundredOrThreeOTwo(String link) {
		int code = 0;
		Reporter.log("Link: " + link);
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			code = connection.getResponseCode();
			Reporter.log("Code: " + code);
		} catch (Exception e) {
			Reporter.log(e.toString());
			return false;
		}
		if (link.contains("pager") || code == 403) {
			return true;
		}
		return code == 200 || code == 302;
	}

	public void setWaitTime(WebDriver driver, int waitTime) {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTime));
	}

	public void setWaitTimeToZero(WebDriver driver) {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
	}

	public void customizableCondition(WebDriver driver, int waitTime, final Boolean condition) {
		// setWaitTimeToZero(driver);
		new WebDriverWait(driver, Duration.ofSeconds(waitTime)).until(new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				return condition;
			}
		});
		// setWaitTime(driver, DEFAULT_WAIT_4_ELEMENT);
	}

	public WebElement waitForElementClickable(WebElement webElement, int timeOutInSeconds) {
		WebElement element;
		try {
			// setWaitTimeToZero(driver);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
			element = wait.until(ExpectedConditions.elementToBeClickable(webElement));

			setWaitTime(driver, Constants.DEFAULT_ELEMENT_TIME_OUT);
			return element;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public WebElement waitForElementPresent(final By by, int timeOutInSeconds) {
		WebElement element;
		try {

			// setWaitTimeToZero(driver);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
			element = wait.until(ExpectedConditions.presenceOfElementLocated(by));

			// setWaitTime(driver, DEFAULT_WAIT_4_ELEMENT);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public WebElement waitForElementPresent(WebElement webElement, int timeOutInSeconds) {
		WebElement element;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
			element = wait.until(ExpectedConditions.visibilityOf(webElement));
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean waitForTextPresentInElement(WebElement webElement, String text, int timeOutInSeconds) {
		boolean notVisible;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
		notVisible = wait.until(ExpectedConditions.textToBePresentInElement(webElement, text));

		return notVisible;
	}

	public boolean waitForTextPresentInElement(By by, String text, int timeOutInSeconds) {
		boolean notVisible;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
		notVisible = wait.until(ExpectedConditions.textToBePresentInElementLocated(by, text));

		return notVisible;
	}

	public Boolean isElementPresent(WebElement element) {
		try {
			_waitForJStoLoad();
			waitForElementVisible(element);
			scrollToElement(element);
			element.isDisplayed();
			return true;
		} catch (Exception ex) {
		}
		return false;
	}

	public boolean isAlertPresent() {
		boolean presenceOfAlert = false;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			presenceOfAlert = true;
		} catch (TimeoutException e) {
			presenceOfAlert = false;
		}
		return presenceOfAlert;
	}

	public Boolean isElementPresent(String locator) {
		Boolean result = false;
		try {
			getDriver().findElement(ByLocator(locator));
			result = true;
		} catch (Exception ex) {
		}
		return result;
	}

	public void WaitForElementNotPresent(String locator, int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (!isElementPresent(locator)) {
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void WaitForElementNotPresent(WebElement element, int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (!isElementPresent(element)) {
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void WaitForElementPresent(String locator, int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (isElementPresent(locator)) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int findNumberOfSpecificElementsInContainer(By container, By element) {
		WebElement mainDiv = driver.findElement(container);
		List<WebElement> divs = mainDiv.findElements(element);
		return divs.size();
	}

	public WebDriver hoverOverElementAndClick(WebElement toBeHovered, WebElement toBeClicked) {
		waitForPageLoaded();
		Actions builder = new Actions(driver);
		Action dd = builder.moveToElement(toBeHovered).build();
		dd.perform();
		waitForElementPresent(toBeClicked, Constants.DEFAULT_ELEMENT_TIME_OUT);
		waitForElementVisible(toBeClicked);
		waitAndClick(toBeClicked);
		return driver;
	}

	public WebDriver mouseClick(WebElement element) {

		Actions builder = new Actions(driver);
		builder.click(element).build().perform();
		return driver;
	}

	/**
	 * Select element by visible text
	 * 
	 * @param element
	 * @param targetValue
	 */
	public void selectDropDownByText(WebElement element, String targetValue) {
		waitForElementVisible(element);
		new Select(element).selectByVisibleText(targetValue);
	}

	/*
	 * 
	 * Get random input text
	 */
	public String getRandomName(String field) {

		String randomvalue = field + Utilities.generateRandomString(3);
		return randomvalue;
	}

	/**
	 * Select element by Index
	 * 
	 * @param element
	 * @param index
	 */
	public void selectDropDownByIndex(WebElement element, int index) {
		waitForElement(element);
		new Select(element).selectByIndex(index);
	}

	/**
	 * Select element by value
	 * 
	 * @param element
	 * @param targetValue
	 */
	public void selectDropDownByValue(WebElement element, String targetValue) {
		waitForElement(element);
		new Select(element).selectByValue(targetValue);
	}

	public void waitForElementToBecomeVisible(By by, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIME_OUT));
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public void waitForElementToBecomeVisible(WebElement element, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIME_OUT));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public boolean waitForElementToBecomeVisible(List<WebElement> element, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIME_OUT));
			wait.until(ExpectedConditions.visibilityOfAllElements(element));
			return true;
		} catch (Exception ex) {
		}
		return false;

	}

	public Boolean waitForElementToBecomeInvisible(WebElement element) {
		logger.info(getElementText(element));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		wait.until(ExpectedConditions.invisibilityOf(element));
		return true;
	}

	public void waitForAjaxRequestsToComplete() {
		(new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIME_OUT)))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						JavascriptExecutor js = (JavascriptExecutor) d;
						return (Boolean) js.executeScript("return jQuery.active == 0");
					}
				});
	}

	public void waitForPageLoaded() {
		try {
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			};
			Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
			wait.until(expectation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isTextPresentOnPage(String text) {
		return driver.findElement(By.tagName("body")).getText().contains(text);
	}

//	public boolean isFileAvailableForDownload(WebElement webElement) throws Exception {
//		int code = 0;
//		String downloadUrl = webElement.getAttribute("href");
//		URL url = new URL(downloadUrl);
//		connection =  (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("GET");
//		code = connection.getResponseCode();
//		Reporter.log("The response code for download is " + code);
//		return code == 200;
//	}

//	public void verifyDownloadLink(WebElement downloadLink, int timeOut, String fileName) {
//
//		try {
//			waitForElementClickable(downloadLink, timeOut);
//			Assert.assertTrue(isFileAvailableForDownload(downloadLink));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Assert.assertTrue(getAttibuteValue(downloadLink, "href").contains(fileName));
//	}

	public void takeRemoteWebDriverScreenShot(String fileName) {
		File screenshot = ((TakesScreenshot) new Augmenter().augment(driver)).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshot, new File(fileName));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void waitForTextNotToBeVisible(String text, long timeoutInSeconds) {
		int startWait = 0;
		while (isTextPresentOnPage(text)) {
			// Utils.hardWaitSeconds(1);
			startWait++;
			if (startWait == timeoutInSeconds) {
				throw new TimeoutException();
			}
		}
	}

	public void waitForWebElementPresent(WebElement element) {
		WebDriverWait ajaxWait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_ELEMENT_TIME_OUT));
		ajaxWait.until(ExpectedConditions.visibilityOf(element));
	}

	public Boolean selectRadioBtn(WebElement element) {

		boolean flag = element.isSelected();
		try {
			if (!flag) {
				element.click();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return flag;
	}

	public static int generateRandomNumber(int min, int max) {
		int randomNum = ThreadLocalRandom.current().nextInt(min, max);
		return randomNum;
	}

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void verifyElementPresent(WebElement element) {
		waitForPageLoaded();
		waitForWebElementPresent(element);
		Assert.assertTrue(isElementPresent(element), element.toString() + " is not present");
	}

	public void verifyElementNotPresent(WebElement element) {
		_waitForJStoLoad();
		Assert.assertFalse(isElementPresent(element), element.toString() + " is present");

	}

	public void verifyElementTextNotPresent(WebElement element, String text) {
		_waitForJStoLoad();
		Assert.assertFalse(isElementPresent(element), element.toString() + " is present");
		Assert.assertEquals(element.getText(), text);

	}

	public void verifyElementText(WebElement element, String text) {
		_waitForJStoLoad();
		Assert.assertTrue(isElementPresent(element), element.toString() + " is not present");
		Assert.assertEquals(element.getText(), text);
	}

	public boolean verifyElementValue(WebElement element, String text) {

		Assert.assertTrue(isElementPresent(element), element.toString() + " is not present");
		Assert.assertEquals(element.getAttribute("alt"), text);
		return true;
	}

	public void verifyFileUploaded(WebElement element, String fileName) {
		_waitForJStoLoad();
		Assert.assertTrue(isElementPresent(element), element.toString() + " is not present");
		Assert.assertTrue(element.getAttribute("title").contains(fileName));
	}

	public void verifyElementsText(List<WebElement> element, String text) {
		_waitForJStoLoad();
		boolean status = false;
		for (int i = 0; i < element.size(); i++) {
			String actualText = element.get(i).getText().trim();
			if (actualText.isEmpty())
				actualText = element.get(i).getAttribute("value");
			if (actualText.equalsIgnoreCase(text)) {
				status = true;
				break;
			}
		}

		Assert.assertTrue(status, element.toString() + " Text not present");
	}

	public void verifyElementsTextNot(List<WebElement> element, String text) {
		_waitForJStoLoad();
		boolean status = false;
		for (int i = 0; i < element.size(); i++) {
			String actualText = element.get(i).getText().trim();
			if (actualText.isEmpty())
				actualText = element.get(i).getAttribute("value");
			if (actualText.equalsIgnoreCase(text)) {
				status = true;
				break;
			}
		}

		Assert.assertFalse(status, element.toString() + " Text present");
	}

	public void verifyTextMatch(String string1, String string2) {
		_waitForJStoLoad();
		Assert.assertEquals(string1, string2, string1 + " and " + string2 + " are not matched");

	}

	/**
	 * Right click and select element
	 * 
	 * @param element
	 * @param elementToSelect
	 */
	public void rightClickAndSelect(WebElement element, WebElement elementToSelect) {
		_waitForJStoLoad();
		Actions action = new Actions(driver).contextClick(element);
		action.build().perform();
		clickOn(elementToSelect);

	}

	public void doubleClickOnWebElement(WebElement element) throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		Actions action = new Actions(driver);
		action.doubleClick(element);
		action.perform();
	}

	public String getAttibuteValue(WebElement element, String attributeName) {
		String attibuteValue = element.getAttribute(attributeName);
		return attibuteValue;
	}

	/**
	 * Right click and select first level element then click on second level element
	 * 
	 * @param element
	 * @param elementFirstLevelToSelect
	 * @param elementSecondLevelToClick
	 */
	public void rightClickAndSelectSecondLevelOption(WebElement element, WebElement elementFirstLevelToSelect,
			WebElement elementSecondLevelToClick) {
		_waitForJStoLoad();
		Actions action = new Actions(driver).contextClick(element);
		action.build().perform();
		clickOn(elementFirstLevelToSelect);
		clickOn(elementSecondLevelToClick);

	}

	/**
	 * wait for spinner
	 * 
	 * @throws InterruptedException
	 */
	public void waitForSpinner() {
		String spinnerText = "Please wait while page is being refreshed...";

		for (int i = 0; i <= Constants.DEFAULT_ELEMENT_TIME_OUT; i++) {
			waitForElement(driver.findElement(By.xpath("//div[@class='ProgressBar']")));
			String spinnerElement = getElement("//div[@class='ProgressBar']").getCssValue("display");
			if (!spinnerElement.contains("none")) {
				try {
					waitForTextNotToBeVisible(spinnerText, Constants.DEFAULT_ELEMENT_TIME_OUT);
					Thread.sleep(2000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			} else {
				break;
			}

		}
	}

	/**
	 * Drag the first element and drop to another element.
	 * 
	 * @param onElement
	 * @param toElement
	 */
	public void dragAndDrop(WebElement onElement, WebElement toElement) {
		_waitForJStoLoad();
		Actions builder = new Actions(driver);
		builder.clickAndHold(onElement).moveToElement(toElement).release(toElement).build().perform();

	}

	/**
	 * Verify two ArrayList matched.
	 * 
	 * @param list1
	 * @param list2
	 */
	public void verifyListMatch(ArrayList<?> list1, ArrayList<?> list2) {
		_waitForJStoLoad();
		Assert.assertEquals(list1, list2, "List are not matched");
	}

	/**
	 * ArrayList sorting as Ascending order.
	 * 
	 * @param list
	 * @return
	 */
	public ArrayList<?> sortingAscending(ArrayList<?> list) {
		list.sort(Collections.reverseOrder().reversed());
		return list;
	}

	/**
	 * ArrayList sorting as Descending order.
	 * 
	 * @param list
	 * @return
	 */
	public ArrayList<?> sortingDescending(ArrayList<?> list) {
		list.sort(Collections.reverseOrder());
		return list;
	}

	/**
	 * verify CheckBox is checked.
	 * 
	 * @param element
	 */
	public void verifyChecked(WebElement element) {
		_waitForJStoLoad();
		Assert.assertEquals(element.getAttribute("checked"), "true",
				element.toString() + "The checkbox is not checked");
	}

	/**
	 * verify CheckBoxes are checked.
	 * 
	 * @param element
	 */
	public void clickAllCheckboxes(List<WebElement> elements) {
		for (WebElement el : elements) {
			if (!el.isSelected())
				el.click();
		}
	}

	/**
	 * verify CheckBoxes are checked.
	 * 
	 * @param element
	 */
	public void uncheckAllCheckboxes(List<WebElement> elements) {
		for (WebElement el : elements) {
			if (el.isSelected())
				el.click();
		}
	}

	/**
	 * Check and verify CheckBox are checked.
	 * 
	 * @param element
	 */
	public void clickCheckbox(WebElement element) {
		try {
			if (!element.isSelected()) {
				element.click();
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public WebElement getElement(String locator) {
		return getDriver().findElement(ByLocator(locator));

	}

	public WebElement getAccountAttributeElement(String locator) {
		return getDriver().findElement(By.xpath(
				"//span[@id='ctl00_ctl00_ContentPlaceHolder1_cphRecProcess_ucAccountInfo_lbl" + locator + "Value']"));

	}

	public WebElement getElement(List<WebElement> elements, String text) {
		_waitForJStoLoad();
		WebElement element = null;
		for (int i = 0; i < elements.size(); i++) {
			String actualText = elements.get(i).getText();
			if (actualText.equalsIgnoreCase(""))
				actualText = elements.get(i).getAttribute("value");
			if (actualText.equalsIgnoreCase(text)) {
				element = elements.get(i);
				break;
			}
		}
		return element;
	}

	public WebElement getFirstElement(List<WebElement> elements) {
		_waitForJStoLoad();
		for (WebElement element : elements) {
			if (element.isDisplayed()) {
				return element;
			}
		}
		return null;
	}

	public String getElementText(WebElement element) {
		waitForElement(element);
		String actualText = element.getText().trim();
		if (actualText.isEmpty())
			actualText = element.getAttribute("value");
		return actualText;
	}

	public String getElementValue(WebElement element) {
		waitForElement(element);
		String actualText = element.getAttribute("alt");
		if (actualText.isEmpty())
			actualText = element.getText().trim();
		return actualText;
	}

	public int getElementCount(List<WebElement> elements) {
		return elements.size();
	}

	public void verifyNumberMatched(int firstNumber, int secondNumber) {
		Assert.assertEquals(firstNumber, secondNumber, firstNumber + " and " + secondNumber + " number not matched");

	}

	/**
	 * scroll to element
	 * 
	 * @param element
	 */
	public void scrollToElement(WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView();", element);
	}

	/**
	 * click on main menu item
	 * 
	 * @param toBeHovered
	 * @param toBeClicked
	 */
	public void clickOnMainMenuItem(WebElement toBeHovered, WebElement toBeClicked) {
		this.scrollToElement(toBeClicked);
		this.hoverOverElementAndClick(toBeHovered, toBeClicked);
	}

	public void applyWait(int sec) throws InterruptedException {
		Thread.sleep(sec);
	}

	public List<WebElement> getDropDownOptions(WebElement element) {
		waitForElement(element);
		_waitForJStoLoad();
		List<WebElement> options = new Select(element).getOptions();
		return options;
	}

	/**
	 * Refresh page
	 */
	public void refreshPage() {
		getDriver().navigate().refresh();
	}

	/**
	 * Wait for alert present
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	public void waitForAlert(WebDriver driver) throws InterruptedException {
		int i = 0;
		while (i++ < 5) {
			try {
				Alert alert = driver.switchTo().alert();
				break;
			} catch (NoAlertPresentException e) {
				Thread.sleep(1000);
				continue;
			}
		}
	}

	/**
	 * Scroll to the bottom of screen
	 */
	public void scrollToBottom() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * Scroll to the top of screen
	 */
	public void scrollTop() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,-document.body.scrollHeight)");
	}

	/*
	 * 
	 */
	public List<String> getElementsText(List<WebElement> elements) {

		List<String> arr = new ArrayList<String>();
		for (WebElement element : elements) {
			String sText = getElementText(element);
			logger.info(sText);
			arr.add(sText);
		}
		return arr;

	}

	/* get element based on Text */

	public WebElement getElementBasedOnText(String text) {

		WebElement element = driver.findElement(By.xpath("//input[contains(@id,'" + text + "')]"));

		if (element == null)
			element = driver.findElement(By.xpath("//input[contains(text(),'" + text + "']"));

		return element;

	}

	public WebElement getClickingLoc(String text) {

		return driver.findElement(By.xpath("//input[@value='" + text + "']"));

	}

	/* get element based on Text */

	public WebElement selectElementById(String text) {

		return driver.findElement(By.xpath("//select[contains(@id,'" + text + "')]"));

	}

	

	public List<String> getDropDownOptionsByText(WebElement element) {

		List<WebElement> options = getDropDownOptions(element);
		int count = getElementCount(options);
		logger.info("Total number of periods: " + count);
		List<String> dropdownTexts = getElementsText(options);
		return dropdownTexts;
	}

	public Date stringToDateformat(String str) {
		String date = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date);
		try {
			return simpleDateFormat.parse(str);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;
	}	
	
	 /* This method used to verify color code*/
	public String ColorVerify(WebElement element, String cssValue)
	{   
	   
	    String colorCode= element.getCssValue(cssValue);
	    String hexacolor = Color.fromString(colorCode).asHex();
	    return hexacolor;   
	}

}
