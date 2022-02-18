package com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.BusinessLogic.UserRegistretionLogic;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRegistrationResponseCommon;

@RequestScoped
public class AsyncUserRegistrationLogicCall {
	
	//UserRegistrationLogicクラスのインスタンス化
	UserRegistretionLogic userRegistrationLogic = new UserRegistretionLogic();
	
	//デフォルトコンストラクタ
	public AsyncUserRegistrationLogicCall() {
		
	}
	
	public Response CallAsyncUserRegistrationLogicService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRegistrationResponseCommon userRegistrationResponseCommon = UserRegistrationResponseCommon.RegistAsyncS200;
		
		Future<Response> callAsyncUserRegistration = userRegistrationLogic.F_RegistService(requestEntity, requestBodyEntity);
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

}
