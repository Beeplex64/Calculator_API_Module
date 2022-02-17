package com.src.devcalc.jp.devcalc.ResponseContent;

public class GenerateJWTResponseDetails {

	private String status;
	
	private String message;
	
	private String time;
	
	private String token;
	
	public GenerateJWTResponseDetails() {
		this.setStatus(new String());
		this.setMessage(new String());
		this.setTime(new String());
		this.setToken(new String());
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
