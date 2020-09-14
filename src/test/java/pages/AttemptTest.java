package pages;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.helperClasses.ExcelHelper;
import com.helperClasses.ProcessString;
import com.helperClasses.WaitHelpers;
import com.helperClasses.WaitParams;

public class AttemptTest {
	WebDriver driver;
	Map<String, String> xpathMap = new HashMap<String, String>();
	Map<String, String> results = new HashMap<String, String>();
	WaitHelpers wait = new WaitHelpers();

	public AttemptTest(WebDriver driver, Map<String, String> xpathMap) {
		this.driver = driver;
		this.xpathMap = xpathMap;
	}

	public boolean isAttemptTest() {
		boolean result = false;
		WaitParams param = new WaitParams();
		param.setParamters(10, xpathMap.get("question_paper_title"));
		result = wait.locateElementOnPage(driver, param);
		return result;
	}

	private boolean verifyScore(String xpath, String mapKey) {
		boolean result = false;
		WaitParams field = new WaitParams();

		try {
			field.setParamters(5, xpath);
			if (wait.locateElementOnPage(driver, field)) {
				result = driver.findElement(By.xpath(field.getXpath())).getText().trim().equals(results.get(mapKey));
			}

		} catch (Exception ex) {
			System.out.println("Exception while verifying score : " + ex.getLocalizedMessage());
		}
		return result;
	}

	private boolean isScoreCorrect() {
		boolean result = false;
		result = (verifyScore(xpathMap.get("result_page_correct_answers"), "Correct")
				&& verifyScore(xpathMap.get("result_page_incorrect_answers"), "Incorrect")
				&& verifyScore(xpathMap.get("result_page_unattemped_questions"), "Unattempted"));
		return result;
	}

	private boolean submitTest() {
		boolean result = false;
		WaitParams fieldValue = new WaitParams();
		WaitParams fieldExpected = new WaitParams();
		WaitParams fieldClicked = new WaitParams();

		fieldClicked.setParamters(10, xpathMap.get("submit_answers"));
		fieldExpected.setParamters(10, xpathMap.get("submit_test_confirmation"));

		if (wait.clickAndWait(driver, fieldClicked, fieldClicked)) {
			try {
				results.put("Correct", driver.findElement(By.xpath(xpathMap.get("submit_notif_correct"))).getText());
				results.put("Incorrect",
						driver.findElement(By.xpath(xpathMap.get("submit_notif_incorrect"))).getText());
				results.put("Unattempted",
						driver.findElement(By.xpath(xpathMap.get("submit_notif_unattempted"))).getText());

			} catch (Exception ex) {
				System.out.println("Exception while getting data from submit Confirmation >> " + ex.getMessage());

			} finally {
				fieldClicked.setParamters(10, xpathMap.get("submit_notif_submit_test"));
				fieldExpected.setParamters(10, xpathMap.get("test_performance_pageHeader"));
				result = wait.clickAndWait(driver, fieldClicked, fieldExpected);
			}
		}
		return result;
	}

	private void selectAnswers(int numberOfQuestions) {
		ProcessString convert = new ProcessString();
		WaitParams fieldValue = new WaitParams();
		
		fieldValue.setTime(10);
		
		for (int q = 1; q <= numberOfQuestions; q++) {
			String newPath = convert.createXpath(xpathMap.get("questions"), "$id", "q-" + String.valueOf(q));
			fieldValue.setXpath(newPath);
			
			// System.out.println("<<New Path for Q"+q+">>"+newPath);
			try {
				Thread.sleep(200);
				if (wait.locateElementOnPage(driver, fieldValue)) {
					newPath = convert.createXpath(xpathMap.get("answer_options"), "$id", "q-" + String.valueOf(q));
					int index = q%5;
					if (index == 0) {
						index = index + 1;
					}
					newPath = convert.createXpath(newPath, "$option", String.valueOf(index));
					fieldValue.setXpath(newPath);
					wait.waitAndClick(driver, fieldValue);

				}
			} catch (Exception ex) {
				System.out.println("Exception while answering Question\n" + ex.getMessage());
			}
		}
	}

	private void exportResultsToExcel() {
		ExcelHelper workBook = new ExcelHelper();
		String Score = "", Rank = "", Cutoff = "";
		WaitParams fieldValue = new WaitParams();

		Map<String, String> exportResult = new HashMap<String, String>();

		fieldValue.setParamters(5, xpathMap.get("score_on_report"));
		if (wait.locateElementOnPage(driver, fieldValue)) {
			Score = driver.findElement(By.xpath(fieldValue.getXpath())).getText();
		}

		fieldValue.setXpath(xpathMap.get("rank_on_report"));
		if (wait.locateElementOnPage(driver, fieldValue)) {
			Rank = driver.findElement(By.xpath(fieldValue.getXpath())).getText();
		}

		fieldValue.setXpath(xpathMap.get("cut_off_for_exam"));
		if (wait.locateElementOnPage(driver, fieldValue)) {
			Cutoff = driver.findElement(By.xpath(fieldValue.getXpath())).getText();
		}
		if (!Score.equals("") && !Rank.equals("") && !Cutoff.equals("")) {
			exportResult.put("Score", Score);
			exportResult.put("Rank", Rank);
			exportResult.put("CutOff", Cutoff);
			workBook.exportResults("/target/testOutput/Result.xls", exportResult);
		}
	}

	public boolean attemptTest() {
		boolean result = false;
		if (isAttemptTest()) {
			selectAnswers(10);
			if (submitTest()) {
				result = isScoreCorrect();
				exportResultsToExcel();
			}
		}
		return result;
	}
}
