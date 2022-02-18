package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum SubtractionResponseCommon {
	
	//SuccessResponse
	SubtractionS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "3.SubtractionCalcAPI"),
	//ErrorResponse
	SubtractionE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "3.SubtractionCalcAPI"),
	SubtractionE401(Response.Status.UNAUTHORIZED.getStatusCode(), "E401", "システムエラーが発生しました。管理者に問い合わせてください。", "3.SubtractionCalcAPI"),
	SubtractionE404(Response.Status.NOT_FOUND.getStatusCode(), "E404", "システムエラーが発生しました。管理者に問い合わせてください。", "3.SubtractionCalcAPI"),
	SubtractionE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "3.SubtractionCalcAPI");
	
	private int Status;
	
	private String SubtractionResponseStatus;
	
	private String SubtractionResponseMessage;
	
	private String SubtractionResponseAPINo;
	
	private SubtractionResponseCommon(int Status, String SubtractionResponseStatus, String SubtractionResponseMessage, String SubtractionResponseAPINo) {
		this.Status = Status;
		this.SubtractionResponseStatus = SubtractionResponseStatus;
		this.SubtractionResponseMessage = SubtractionResponseMessage;
		this.SubtractionResponseAPINo = SubtractionResponseAPINo;
	}
	
	public int getStatus() {
		return Status;
	}
	
	public String getSubtractionResponseStatus() {
		return SubtractionResponseStatus;
	}
	
	public String getSubtractionResponseMessage() {
		return SubtractionResponseMessage;
	}
	
	public String getSubtractionResponseAPINo() {
		return SubtractionResponseAPINo;
	}
}
