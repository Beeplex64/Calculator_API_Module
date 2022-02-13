package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum AdditionResponseCommon {
	
	//SuccessResponse
	AdditionS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "1.AdditionCalcAPI"),
	//ErrorResponse
	AdditionE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "1.AdditionCalcAPI"),
	AdditionE401(Response.Status.UNAUTHORIZED.getStatusCode(), "E401", "システムエラーが発生しました。管理者に問い合わせてください。", "1.AdditionCalcAPI"),
	AdditionE404(Response.Status.NOT_FOUND.getStatusCode(), "E404", "システムエラーが発生しました。管理者に問い合わせてください。", "1.AdditionCalcAPI"),
	AdditionE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "1.AdditionCalcAPI");
	
	private int Status;
	
	private String AdditionResponseStatus;
	
	private String AdditionResponseMessage;
	
	private String AdditionResponseAPINo;
	
	private AdditionResponseCommon(int Status, String AdditionResponseStatus, String AdditionResponseMessage, String AdditionResponseAPINo) {
		this.Status = Status;
		this.AdditionResponseStatus = AdditionResponseStatus;
		this.AdditionResponseMessage = AdditionResponseMessage;
		this.AdditionResponseAPINo = AdditionResponseAPINo;
	}
	
	public int getStatus() {
		return Status;
	}
	
	public String getAdditionResponseStatus() {
		return AdditionResponseStatus;
	}
	
	public String getAdditionResponseMessage() {
		return AdditionResponseMessage;
	}
	
	public String getAdditionResponseAPINo() {
		return AdditionResponseAPINo;
	}
}
