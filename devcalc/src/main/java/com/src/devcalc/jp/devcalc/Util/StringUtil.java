package com.src.devcalc.jp.devcalc.Util;

public class StringUtil {
	
	private String string;
	
	public StringUtil() {
		
	}
	
	public String getstring() {
		return string;
	}
	
	public void setstring(String string) {
		this.string = string;
	}
	
	public boolean isNullorEmptytoString(String string) {
		return (string == null || 0 == string.length());
	}

}
