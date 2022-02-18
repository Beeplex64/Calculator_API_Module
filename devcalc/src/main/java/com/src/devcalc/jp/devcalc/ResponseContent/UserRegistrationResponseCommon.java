package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum UserRegistrationResponseCommon {
	
	//SuccessResponse
	RegistAsyncS200(Response.Status.OK.getStatusCode(), "S200", "登録を受け付けました。 メールが届かない場合は、既にそのユーザーIDが利用されているかシステムで何らかのエラーが発生している可能性がございますので、再度ご登録をお願いいたします。", "5.UserRegistrationAPI"),
	RegistS201(Response.Status.CREATED.getStatusCode(), "S201", "SUCCESS", "5.UserRegistrationAPI"),
	//ErrorResponse
	RegistE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "5.UserRegistrationAPI"),
	RegistE409(Response.Status.CONFLICT.getStatusCode(), "E409", "既にそのユーザーIDは利用されています。", "5.UserRegistrationAPI"),
	RegistE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "5.UserRegistrationAPI");
	
	private int Status;
	
	private String RegistResponseStatus;
	
	private String RegistResponseMessage;
	
	private String RegistResponseAPINo;
	
	private UserRegistrationResponseCommon(int Status, String RegistResponseStatus, String RegistResponseMessage, String RegistResponseAPINo) {
		this.Status = Status;
		this.RegistResponseStatus = RegistResponseStatus;
		this.RegistResponseMessage = RegistResponseMessage;
		this.RegistResponseAPINo = RegistResponseAPINo;
	}
	
	public int getStatus() {
		return Status;
	}
	
	public String getRegistResponseStatus() {
		return RegistResponseStatus;
	}
	
	public String getRegistResponseMessage() {
		return RegistResponseMessage;
	}
	
	public String getRegistResponseAPINo() {
		return RegistResponseAPINo;
	}

}
