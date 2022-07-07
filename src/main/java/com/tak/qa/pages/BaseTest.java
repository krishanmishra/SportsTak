package com.tak.qa.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class BaseTest {

	private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

	protected WebDriver driver;
	protected String applicationUrl;

	/* pages object initialization */

	protected HomePage homePage;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	@BeforeMethod()
	public void setUp(Method method) throws Exception {

		WebDriverManager.chromedriver().setup();
//		driver = new ChromeDriver();
		tlDriver.set(new ChromeDriver());

		/* Delete cookies */
		getWebDriver().manage().deleteAllCookies();

		/* maximize the browser */
		getWebDriver().manage().window().maximize();

		/* open application URL */

		homePage = PageFactory.initElements(getWebDriver(), HomePage.class);

	}

	@AfterMethod()
	public void captureScreenShot(ITestResult result) throws IOException, InterruptedException {
		if (result.getStatus() == ITestResult.FAILURE) {
			captureScreenshot(result);
		}
	}

	

	@AfterSuite(alwaysRun = true)
	public void tearDownSuite() {

		try {
			getWebDriver().quit();
			logger.info("Quit the browser");
		
		} catch (Exception e) {
			System.out.println("Driver error preventing from Quitting.");
		}

	}

	/* Return WebDriver */
	public WebDriver getWebDriver() {
		return tlDriver.get();
		
	}

	/* capturing screenshot */
	public void captureScreenshot(ITestResult result) throws IOException, InterruptedException {
		try {
			String screenshotName = Utilities.getFileName(result.getName());
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String path = Utilities.getPath();
			String screen = path + "/screenshots/" + System.currentTimeMillis() + ".png";
			File screenshotLocation = new File(screen);
			FileUtils.copyFile(screenshot, screenshotLocation);
			Thread.sleep(2000);
			InputStream is = new FileInputStream(screenshotLocation);
			byte[] imageBytes = IOUtils.toByteArray(is);
			Thread.sleep(2000);
			String base64 = Base64.getEncoder().encodeToString(imageBytes);
			
			Reporter.log(
					"<a href= '" + screen + "'target='_blank' ><img src='" + screen + "'>" + screenshotName + "</a>");
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	public void onTestFailure(ITestResult result) {
		System.out.println("*** Test execution " + result.getMethod().getMethodName() + " failed...");
		System.out.println(result.getMethod().getMethodName() + " failed!");

		ITestContext context = result.getTestContext();
		WebDriver driver = (WebDriver) context.getAttribute("driver");

		// attach screenshots to report
		saveFailureScreenShot(driver);
	}

	public byte[] saveFailureScreenShot(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	/* Report logs */
	public void reportLog(String message) {
		
		// message = BREAK_LINE + message;
		// logger.info("Message: " + message);
		Reporter.log(message);
	}

	public static Logger getLogger() {
		return logger;
	}

}
