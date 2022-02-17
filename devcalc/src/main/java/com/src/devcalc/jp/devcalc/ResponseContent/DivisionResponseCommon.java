package com.src.devcalc.jp.devcalc.ResponseContent;

import javax.ws.rs.core.Response;

public enum DivisionResponseCommon {

	//SuccessResponse
		DivisionS200(Response.Status.OK.getStatusCode(), "S200", "SUCCESS", "4.DivisionCalcAPI"),
		//ErrorResponse
		DivisionE400(Response.Status.BAD_REQUEST.getStatusCode(), "E400", "システムエラーが発生しました。管理者に問い合わせてください。", "4.DivisionCalcAPI"),
		DivisionE401(Response.Status.UNAUTHORIZED.getStatusCode(), "E401", "システムエラーが発生しました。管理者に問い合わせてください。", "4.DivisionCalcAPI"),
		DivisionE404(Response.Status.NOT_FOUND.getStatusCode(), "E404", "システムエラーが発生しました。管理者に問い合わせてください。", "4.DivisionCalcAPI"),
		DivisionE500(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "E500", "システムエラーが発生しました。管理者に問い合わせてください。", "4.DivisionCalcAPI");
		
		private int Status;
		
		private String DivisionResponseStatus;
		
		private String DivisionResponseMessage;
		
		private String DivisionResponseAPINo;
		
		private DivisionResponseCommon(int Status, String DivisionResponseStatus, String DivisionResponseMessage, String DivisionResponseAPINo) {
			this.Status = Status;
			this.DivisionResponseStatus = DivisionResponseStatus;
			this.DivisionResponseMessage = DivisionResponseMessage;
			this.DivisionResponseAPINo = DivisionResponseAPINo;
		}
		
		public int getStatus() {
			return Status;
		}
		
		public String getDivisionResponseStatus() {
			return DivisionResponseStatus;
		}
		
		public String getDivisionResponseMessage() {
			return DivisionResponseMessage;
		}
		
		public String getDivisionResponseAPINo() {
			return DivisionResponseAPINo;
		}
}
