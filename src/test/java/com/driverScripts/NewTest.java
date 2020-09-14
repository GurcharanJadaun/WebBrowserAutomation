package com.driverScripts;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.capabilitySetup.SetupCapability;
import com.helperClasses.ExcelHelper;
import com.helperClasses.WaitHelpers;

import pages.AttemptTest;
import pages.HomePage;
import pages.LoginPage;

public class NewTest {
	protected static WebDriver driver;
	WaitHelpers wait = new WaitHelpers();
	Map<String, String> xpathMap = new HashMap<String, String>();
	String SignInEmail="",SignInPassword="";

	@BeforeSuite
	public void setupXpathRepository() {
		ExcelHelper workBook = new ExcelHelper();
		xpathMap = workBook.getXpathMap("/src/resources/Repository/gradeupRepository.xls");
	}
@Parameters({"SignInEmail","SignInPassword"})
	@BeforeTest
	public void setupTest(String Email,String Password) {
	SignInEmail=Email;
	SignInPassword=Password;
		SetupCapability set = new SetupCapability();
		driver = set.setupDriver("Chrome");
		driver.get("https://gradeup.co");
	}

	@Test(priority = 1)
	public void login() {
		LoginPage login = new LoginPage(driver, xpathMap, SignInEmail, SignInPassword);
		System.out.println("Login Result : " + login.loginUsingEmail());
	}

	@Test(priority = 2, dependsOnMethods = { "login" })
	public void openTest() {
		HomePage test = new HomePage(driver, xpathMap);
		System.out.println("Open test : " + test.openPreviousYearPapers());
	}

	@Test(priority = 3, dependsOnMethods = { "openTest" })
	public void attemptTest() {
		AttemptTest test = new AttemptTest(driver, xpathMap);
		boolean result = test.attemptTest();
		Assert.assertTrue(result);
	}

	@AfterMethod
	public void dealWithNotif() {
		try {
			driver.switchTo().alert().dismiss();
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	@AfterTest
	public void close() {
		driver.quit();
	}

}
