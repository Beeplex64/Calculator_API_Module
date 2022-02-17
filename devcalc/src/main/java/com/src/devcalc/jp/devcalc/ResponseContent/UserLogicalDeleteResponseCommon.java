package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum UserLogicalDeleteResponseCommon {
	
	//SuccessResponse
	DeleteS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "7.WithDrawalAPI"),
	//ErrorResponse
	DeleteE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "7.WithDrawalAPI"),
	DeleteE401(Response.Status.UNAUTHORIZED.getStatusCode(), "E401", "システムエラーが発生しました。管理者に問い合わせてください。", "7.WithDrawalAPI"),
	DeleteE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "7.WithDrawalAPI");
	
	private int Status;
	
	private String UserDeleteResponseStatus;
	
	private String UserDeleteResponseMessage;
	
	private String UserDeleteResponseAPINo;
	
	private UserLogicalDeleteResponseCommon(int Status, String UserDeleteResponseStatus, String UserDeleteResponseMessage, String UserDeleteResponseAPINo) {
		this.Status = Status;
		this.UserDeleteResponseStatus = UserDeleteResponseStatus;
		this.UserDeleteResponseMessage = UserDeleteResponseMessage;
		this.UserDeleteResponseAPINo = UserDeleteResponseAPINo;
	}
	
	public int getStatus() {
		return Status;
	}
	
	public String getUserDeleteResponseStatus() {
		return UserDeleteResponseStatus;
	}
	
	public String getUserDeleteResponseMessage() {
		return UserDeleteResponseMessage;
	}
	
	public String getUserDeleteResponseAPINo() {
		return UserDeleteResponseAPINo;
	}

}
