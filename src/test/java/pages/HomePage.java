package pages;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.helperClasses.WaitHelpers;
import com.helperClasses.WaitParams;

public class HomePage {
	WebDriver driver;
	Map<String, String> xpathMap = new HashMap<String, String>();
	WaitHelpers wait = new WaitHelpers();

	public HomePage(WebDriver driver, Map<String, String> xpathMap) {
		this.driver = driver;
		this.xpathMap = xpathMap;
	}

	public boolean isHomePageVisible() {
		boolean result = false;
		WaitParams param = new WaitParams();
		param.setParamters(10, xpathMap.get("loggedIn_user_image"));
		result = wait.locateElementOnPage(driver, param);
		return result;
	}

	public boolean openPreviousYearPapers() {
		boolean result = false;
		WaitParams fieldClicked = new WaitParams();
		WaitParams fieldExpected = new WaitParams();
		fieldClicked.setParamters(10, xpathMap.get("previous_paper_toggle_button"));
		fieldExpected.setParamters(10, xpathMap.get("all_papers_tag"));
		if (wait.clickAndWait(driver, fieldClicked, fieldExpected)) {
			{
				result = attemptFreshTest();
			}

		}
		return result;
	}

	public boolean attemptFreshTest() {
		boolean result = false;
		WaitParams fieldClicked = new WaitParams();
		WaitParams fieldExpected = new WaitParams();
		fieldClicked.setParamters(10, xpathMap.get("start_test_button"));
		fieldExpected.setParamters(10, xpathMap.get("question_paper_title"));
		if (wait.clickAndWait(driver, fieldClicked, fieldExpected)) {

			result = true;

		}else {
			WaitParams viewMore = new WaitParams();
			viewMore.setParamters(10, xpathMap.get("view_more_tests"));
			if (wait.clickAndWait(driver, viewMore, fieldClicked)) {

				if (wait.clickAndWait(driver, fieldClicked, fieldExpected)) {

					result = true;

				}

			}
		}
		return result;
	}
	/*
	 * public boolean retakeTest() { boolean result=false; WaitParams fieldClicked =
	 * new WaitParams(); WaitParams fieldExpected = new WaitParams();
	 * fieldClicked.setParamters(10, xpathMap.get(""));
	 * fieldExpected.setParamters(10, xpathMap.get("question_paper_title"));
	 * if(wait.clickAndWait(driver, fieldClicked, fieldExpected)) { { result=true; }
	 * 
	 * } return result; }
	 */
}
