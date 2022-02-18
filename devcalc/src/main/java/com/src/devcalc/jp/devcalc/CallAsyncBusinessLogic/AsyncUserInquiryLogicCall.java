package com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.BusinessLogic.UserInquiryLogic;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserInquiryResponseCommon;

@RequestScoped
public class AsyncUserInquiryLogicCall {
	
	//UserInquiryLogicクラスのインスタンス化
	UserInquiryLogic userInquiryLogic = new UserInquiryLogic();
	
	//デフォルトコンストラクタ
	public AsyncUserInquiryLogicCall() {
		
	}
	
	public Response CallAsyncUserInquiryLogicService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserInquiryResponseCommon userInquiryResponseCommon = UserInquiryResponseCommon.InquiryAsyncS200;
		
		Future<Response> callAsyncUserInquiry = userInquiryLogic.F_UserInquiryService(requestEntity, requestBodyEntity);
		
		generalResponseDetails.setStatus(userInquiryResponseCommon.getInquiryResponseStatus());
		generalResponseDetails.setMessage(userInquiryResponseCommon.getInquiryResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userInquiryResponseCommon.getInquiryResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

}
