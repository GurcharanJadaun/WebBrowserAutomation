package com.helperClasses;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WaitHelpers {
	public boolean locateElementOnPage(WebDriver driver, WaitParams param) {
		boolean result = false;
		WebDriverWait wait = new WebDriverWait(driver, param.getTime());
		try {
			if(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(param.getXpath())))!=null) {
			WebElement ele=driver.findElement(By.xpath(param.getXpath()));
			if(!ele.isDisplayed()) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView()",ele);}
			Thread.sleep(500);
			result = true;}
		} catch (Exception ex) {
			System.out.println("Exception while locating element with path >>"+param.getXpath()+">>"+ex.getMessage());
			result = false;
		}
		return result;
	}
	public boolean clickAndWait(WebDriver driver, WaitParams buttonParam,WaitParams targetParam) {
		boolean result=false;
		try {
		if(waitAndClick(driver,buttonParam)) {
				result=locateElementOnPage(driver,targetParam);
				
			}
		}catch(Exception ex) {
			System.out.println("Exception>>"+ex.getMessage());
		}
		return result;
	}
	public boolean waitAndClick(WebDriver driver, WaitParams buttonParam) {
		boolean result=false;
		WebDriverWait wait = new WebDriverWait(driver, buttonParam.getTime());
		try {
			if(locateElementOnPage(driver,buttonParam)) {
				WebElement button=driver.findElement(By.xpath(buttonParam.getXpath()));
				if(wait.until(ExpectedConditions.elementToBeClickable(button)) != null) {
					button.click();
					result=true;
					Thread.sleep(500);}
			}
		}catch(Exception ex) {
			System.out.println("Exception>>"+ex.getMessage());
		}
		return result;
		
	}
	public boolean setDataInTextField(WebDriver driver,WaitParams param,String input) {
		boolean result=false;
		if(locateElementOnPage(driver,param))
		{
			try {
				Thread.sleep(200);
				driver.findElement(By.xpath(param.getXpath())).sendKeys(input);
				result=true;
			}catch(Exception ex) {
				System.out.println("Something went wrong while entering data "+ex.getMessage());
			}
		}
		return result;
	}
	 public boolean waitForWindowToClose(WebDriver driver,WaitParams param) {
		 boolean result=false;
		 WebDriverWait wait=new WebDriverWait(driver,param.getTime());
		 result=wait.until(waitForWindowToClose(param.getXpath()));
		 return result;
	 }
	 ExpectedCondition<Boolean> waitForWindowToClose(final String windowHandle) {
		 return new ExpectedCondition<Boolean>(){

			public Boolean apply(WebDriver driver) {
				boolean result=false;
				Set<String> windowHandles=driver.getWindowHandles();
				result=!windowHandles.contains(windowHandle);
				return result;
			}
			 
		 };
	 }
}
