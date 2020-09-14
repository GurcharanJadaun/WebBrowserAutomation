package pages;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;

import com.helperClasses.WaitHelpers;
import com.helperClasses.WaitParams;

public class LoginPage {
	WebDriver driver;
	Map<String, String> xpathMap = new HashMap<String, String>();
	WaitHelpers wait = new WaitHelpers();
	String loginEmail, loginPassword;

	public LoginPage(WebDriver driver, Map<String, String> xpathMap, String loginEmail, String loginPassword) {
		this.driver = driver;
		this.xpathMap = xpathMap;
		this.loginEmail = loginEmail;
		this.loginPassword = loginPassword;
	}

	public boolean isLoginPageVisible() {
		boolean result = false;
		WaitParams param = new WaitParams();
		param.setParamters(10, xpathMap.get("login_page_header"));
		result = wait.locateElementOnPage(driver, param);
		return result;
	}

	public boolean loginUsingEmail() {
		boolean result = false;
		String windowHandle = driver.getWindowHandle();
		WaitParams buttonParam = new WaitParams();
		WaitParams targetParam = new WaitParams();
		buttonParam.setParamters(10, xpathMap.get("signin_button_for_email"));
		targetParam.setParamters(10, xpathMap.get("continue_with_email_header"));
		if (isLoginPageVisible()) {
			System.out.println("LoginPageVisible ");
			if (wait.clickAndWait(driver, buttonParam, targetParam)) {
				buttonParam.setXpath(xpathMap.get("signin_button_for_google_on_email_Alert"));// change the button xpath
				if (wait.waitAndClick(driver, buttonParam)) {
					Set<String> handles = driver.getWindowHandles();
					if (handles.size() > 1) {
						for (String page : handles) {
							if (!page.equals(windowHandle)) {
								driver.switchTo().window(page);
								if (signIntoGmailAccount()) {
									targetParam.setParamters(15,xpathMap.get("loggedIn_user_image"));
									driver.switchTo().window(windowHandle);
									result=wait.locateElementOnPage(driver, targetParam);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private boolean signIntoGmailAccount() {
		boolean result = false;
		WaitParams inputField = new WaitParams();
		WaitParams pageHeader = new WaitParams();
		WaitParams button = new WaitParams();
		inputField.setParamters(10, xpathMap.get("google_signin_input_email"));
		pageHeader.setParamters(10, xpathMap.get("google_signin_welcome"));
		button.setParamters(10, xpathMap.get("google_signin_next"));

		if (wait.setDataInTextField(driver, inputField, loginEmail)) {
			if (wait.clickAndWait(driver, button, pageHeader)) {
				inputField.setXpath(xpathMap.get("google_signin_password"));
				if (wait.setDataInTextField(driver, inputField, loginPassword)) {

					if (wait.waitAndClick(driver, button)) {
						WaitParams param = new WaitParams();
						param.setParamters(30, driver.getWindowHandle());
						result = wait.waitForWindowToClose(driver, param);
					}
				}
			}
		}

		return result;
	}
}
