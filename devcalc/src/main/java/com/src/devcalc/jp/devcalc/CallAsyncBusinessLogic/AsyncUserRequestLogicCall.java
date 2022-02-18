package com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.BusinessLogic.UserRequestLogic;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRequestResponseCommon;

@RequestScoped
public class AsyncUserRequestLogicCall {
	
	//UserRequestLogicクラスのインスタンス化
	UserRequestLogic userRequest = new UserRequestLogic();
	
	//デフォルトコンストラクタ
	public AsyncUserRequestLogicCall() {
		
	}
	
	public Response CallAsyncUserRequestLogicService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRequestResponseCommon userRequestResponseCommon = UserRequestResponseCommon.RequestAsyncS200;
		
		Future<Response> callAsyncUserRequest = userRequest.F_UserRequestService(requestEntity, requestBodyEntity);
		
		generalResponseDetails.setStatus(userRequestResponseCommon.getUserRequestResponseStatus());
		generalResponseDetails.setMessage(userRequestResponseCommon.getUserRequestResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRequestResponseCommon.getUserRequestResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

}
