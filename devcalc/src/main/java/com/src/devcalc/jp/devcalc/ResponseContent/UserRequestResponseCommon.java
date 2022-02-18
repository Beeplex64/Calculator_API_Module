package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum UserRequestResponseCommon {
	
	//SuccessResponse
	RequestS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "11.RequestAPI"),
	RequestAsyncS200(Response.Status.OK.getStatusCode(), "S200", "ご要望を受け付けました。", "11.RequestAPI"),
	//ErrorResponse
	RequestE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "11.RequestAPI"),
	RequestE401(Response.Status.UNAUTHORIZED.getStatusCode(), "E401", "システムエラーが発生しました。管理者に問い合わせてください。", "11.RequestAPI"),
	RequestE403(Response.Status.FORBIDDEN.getStatusCode(), "E403", "ユーザー情報にアクセスする権限がありません。", "11.RequestAPI"),
	RequestE404(Response.Status.NOT_FOUND.getStatusCode(), "E404", "システムエラーが発生しました。管理者に問い合わせてください。", "11.RequestAPI"),
	RequestE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "11.RequestAPI");
	
	private int Status;
	
	private String UserRequestResponseStatus;
	
	private String UserRequestResponseMessage;
	
	private String UserRequestResponseAPINo;
	
	private UserRequestResponseCommon(int Status, String UserRequestResponseStatus, String UserRequestResponseMessage, String UserRequestResponseAPINo) {
		this.Status = Status;
		this.UserRequestResponseStatus = UserRequestResponseStatus;
		this.UserRequestResponseMessage = UserRequestResponseMessage;
		this.UserRequestResponseAPINo = UserRequestResponseAPINo;
	}
	
	public int getStatus() {
		return Status;
	}
	
	public String getUserRequestResponseStatus() {
		return UserRequestResponseStatus;
	}
	
	public String getUserRequestResponseMessage() {
		return UserRequestResponseMessage;
	}
	
	public String getUserRequestResponseAPINo() {
		return UserRequestResponseAPINo;
	}
}
