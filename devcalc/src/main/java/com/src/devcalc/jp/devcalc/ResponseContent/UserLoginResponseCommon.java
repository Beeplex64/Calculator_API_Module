package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum UserLoginResponseCommon {
	
	//SuccessResponse
	LoginS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "6.LoginAPI"),
	//ErrorResponse
	LoginE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "6.LoginAPI"),
	LoginE401(Response.Status.UNAUTHORIZED.getStatusCode(), "E401", "ユーザーのログイン権限がありません。", "6.LoginAPI"),
	LoginE404(Response.Status.NOT_FOUND.getStatusCode(), "E404", "ユーザーID・パスワードが間違っています。", "6.LoginAPI"),
	LoginE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "6.LoginAPI");
	
	private int Status;
	
	private String UserLoginResponseStatus;
	
	private String UserLoginResponseMessage;
	
	private String UserLoginResponseAPINo;
	
	private UserLoginResponseCommon(int Status, String UserLoginResponseStatus, String UserLoginResponseMessage, String UserLoginResponseAPINo) {
		this.Status = Status;
		this.UserLoginResponseStatus = UserLoginResponseStatus;
		this.UserLoginResponseMessage = UserLoginResponseMessage;
		this.UserLoginResponseAPINo = UserLoginResponseAPINo;
	}

	public int getStatus() {
		return Status;
	}
	
	public String getUserLoginResponseStatus() {
		return UserLoginResponseStatus;
	}
	
	public String getUserLoginResponseMessage() {
		return UserLoginResponseMessage;
	}
	
	public String getUserLoginResponseAPINo() {
		return UserLoginResponseAPINo;
	}
}
