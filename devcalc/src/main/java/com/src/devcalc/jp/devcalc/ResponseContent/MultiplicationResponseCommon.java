package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum MultiplicationResponseCommon {

	//SuccessResponse
	MultiplicationS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "2.MultiplicationCalcAPI"),
	//ErrorResponse
	MultiplicationE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "2.MultiplicationCalcAPI"),
	MultiplicationE401(Response.Status.UNAUTHORIZED.getStatusCode(), "E401", "システムエラーが発生しました。管理者に問い合わせてください。", "2.MultiplicationCalcAPI"),
	MultiplicationE404(Response.Status.NOT_FOUND.getStatusCode(), "E404", "システムエラーが発生しました。管理者に問い合わせてください。", "2.MultiplicationCalcAPI"),
	MultiplicationE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "2.MultiplicationCalcAPI");
	
	private int Status;
	
	private String MultiplicationResponseStatus;
	
	private String MultiplicationResponseMessage;
	
	private String MultiplicationResponseAPINo;
	
	private MultiplicationResponseCommon(int Status, String MultiplicationResponseStatus, String MultiplicationResponseMessage, String MultiplicationResponseAPINo) {
		this.Status = Status;
		this.MultiplicationResponseStatus = MultiplicationResponseStatus;
		this.MultiplicationResponseMessage = MultiplicationResponseMessage;
		this.MultiplicationResponseAPINo = MultiplicationResponseAPINo;
	}
	
	public int getStatus() {
		return Status;
	}
	
	public String getMultiplicationResponseStatus() {
		return MultiplicationResponseStatus;
	}
	
	public String getMultiplicationResponseMessage() {
		return MultiplicationResponseMessage;
	}
	
	public String getMultiplicationResponseAPINo() {
		return MultiplicationResponseAPINo;
	}
}
