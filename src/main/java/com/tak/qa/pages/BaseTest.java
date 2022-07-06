package com.tak.qa.pages;

import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;



import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	WebDriver driver;
	
	protected HomePage homePage;

	public SoftAssert softAssert;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	@BeforeTest
	public void setup() {
		driver=init_driver();		
		homePage = new HomePage(driver);
		softAssert = new SoftAssert();
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	/**
	 * this method is used to initialize the webdriver on the basis of given browser
	 * name
	 * 
	 * @param
	 * @return it returns driver
	 */
	public WebDriver init_driver() {

		String browserName = "chrome";

		System.out.println("browser name is : " + browserName);
		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			// driver = new ChromeDriver(optinonsManager.getChromeOptions());
			tlDriver.set(new ChromeDriver());
		}

		else if (browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			// driver = new FirefoxDriver(optinonsManager.getFirefoxOptions());
			tlDriver.set(new FirefoxDriver());

		}

		else {
			System.out.println("Please pass the right browser : " + browserName);
		}

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().get("https://thesportstak.com/");

		return getDriver();
	}

	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	

}
