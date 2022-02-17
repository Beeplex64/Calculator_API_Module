package com.src.devcalc.jp.devcalc.ResponseContent;

public class GeneralResponseDetails {

	private String status;
	
	private String message;
	
	private String time;
	
	private String apino;
	
	public GeneralResponseDetails() {
		this.setStatus(new String());
		this.setMessage(new String());
		this.setTime(new String());
		this.setApino(new String());
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

	public String getApino() {
		return apino;
	}

	public void setApino(String apino) {
		this.apino = apino;
	}
}
