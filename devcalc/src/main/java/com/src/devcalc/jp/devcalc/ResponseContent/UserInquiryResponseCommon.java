package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum UserInquiryResponseCommon {
	
	//SuccessResponse
	InquiryAsyncS200(Response.Status.OK.getStatusCode(), "S200", "お問い合わせを受け付けました。", "12.InquiryAPI"),
	InquiryS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "12.InquiryAPI"),
	//ErrorResponse
	InquiryE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "12.InquiryAPI"),
	InquiryE403(Response.Status.FORBIDDEN.getStatusCode(), "E403", "ユーザー情報にアクセスする権限がありません。", "12.InquiryAPI"),
	InquiryE404(Response.Status.NOT_FOUND.getStatusCode(), "E404", "システムエラーが発生しました。管理者に問い合わせてください。", "12.InquiryAPI"),
	InquiryE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "12.InquiryAPI");
	
	private int Status;
	
	private String InquiryResponseStatus;
	
	private String InquiryResponseMessage;
	
	private String InquiryResponseAPINo;
	
	private UserInquiryResponseCommon(int Status, String InquiryResponseStatus, String InquiryResponseMessage, String InquiryResponseAPINo) {
		this.Status = Status;
		this.InquiryResponseStatus = InquiryResponseStatus;
		this.InquiryResponseMessage = InquiryResponseMessage;
		this.InquiryResponseAPINo = InquiryResponseAPINo;
	}
	
	public int getStatus() {
		return Status;
	}
	
	public String getInquiryResponseStatus() {
		return InquiryResponseStatus;
	}
	
	public String getInquiryResponseMessage() {
		return InquiryResponseMessage;
	}
	
	public String getInquiryResponseAPINo() {
		return InquiryResponseAPINo;
	}

}
