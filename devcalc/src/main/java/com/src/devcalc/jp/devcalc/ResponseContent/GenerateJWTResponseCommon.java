package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum GenerateJWTResponseCommon {
	
	//SuccessResponse
	JWTS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS"),
	//ErrorResponse
	JWTE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。"),
	JWTE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラー発生しました。管理者に問い合わせてください。");
	
	private int Status;
	
	private String GenerateJWTResponseStatus;
	
	private String GenerateJWTResponseMessage;
	
	private GenerateJWTResponseCommon(int Status, String GenerateJWTResponseStatus, String GenerateJWTResponseMessage) {
		this.Status = Status;
		this.GenerateJWTResponseStatus = GenerateJWTResponseStatus;
		this.GenerateJWTResponseMessage = GenerateJWTResponseMessage;
	}

	public int getStatus() {
		return Status;
	}
	
	public String getGenerateJWTResponseStatus() {
		return GenerateJWTResponseStatus;
	}
	
	public String getGenerateJWTResponseMessage() {
		return GenerateJWTResponseMessage;
	}
}
