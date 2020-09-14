package com.capabilitySetup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SetupCapability {

	public WebDriver setupDriver(String browser) {
		DesiredCapabilities caps=new DesiredCapabilities();
		ChromeOptions option=new ChromeOptions();	
		if(browser.equalsIgnoreCase("chrome")) {
		option.addArguments("start-maximized");
		option.merge(caps);
		System.setProperty("webdriver.chrome.driver","src/resources/chromeDriverVer85/chromedriver.exe");}
		WebDriver driver=new ChromeDriver(option);
		return driver;
	}
}
