package com.helperClasses;

public class ProcessString {
	public String createXpath(String xpath,String replace,String newValue) {
		String result = "";
		result=xpath.replace(replace, newValue);
		return result;

	}
}
