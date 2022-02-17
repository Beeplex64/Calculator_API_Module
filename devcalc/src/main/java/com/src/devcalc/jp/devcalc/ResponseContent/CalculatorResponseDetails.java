package com.src.devcalc.jp.devcalc.ResponseContent;

public class CalculatorResponseDetails {
	
	private String status;
	
	private String message;
	
	private String time;
	
	private String result;
	
	private String apino;
	
	public CalculatorResponseDetails() {
		this.setStatus(new String());
		this.setMessage(new String());
		this.setTime(new String());
		this.setResult(new String());
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getApino() {
		return apino;
	}

	public void setApino(String apino) {
		this.apino = apino;
	}
}
