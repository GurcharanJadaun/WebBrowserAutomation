package com.helperClasses;

public class WaitParams {
 private int time;
 private String elementLocator;
 
 public void setParamters(int time,String elementLocator) {
	 this.time=time;
	 this.elementLocator=elementLocator;
 }
 public void setTime(int time) {
	 this.time=time;
 }
 public void setXpath(String elementLocator) {
	 this.elementLocator=elementLocator;
 }

 public int getTime() {
	 return this.time;
 }
 public String getXpath() {
	 return this.elementLocator;
 }
 
}
