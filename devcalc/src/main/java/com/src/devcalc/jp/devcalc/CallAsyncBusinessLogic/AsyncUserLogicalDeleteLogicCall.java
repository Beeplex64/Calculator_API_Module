package com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.BusinessLogic.UserLogicalDeleteLogic;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLogicalDeleteResponseCommon;

@RequestScoped
public class AsyncUserLogicalDeleteLogicCall {
	
	//UserLogicalDeleteLogicクラスのインスタンス化
	UserLogicalDeleteLogic userLogicalDeleteLogic = new UserLogicalDeleteLogic();
	
	//デフォルトコンストラクタ
	public AsyncUserLogicalDeleteLogicCall() {
		
	}
	
	public Response CallAsyncUserLogicalDeleteLogicService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserLogicalDeleteResponseCommon userLogicalDeleteResponseCommon = UserLogicalDeleteResponseCommon.DeleteAsyncS200;
		
		Future<Response> callAsyncUserLogicalDelete = userLogicalDeleteLogic.F_UserDeleteService(requestEntity, requestBodyEntity);
		
		generalResponseDetails.setStatus(userLogicalDeleteResponseCommon.getUserDeleteResponseStatus());
		generalResponseDetails.setMessage(userLogicalDeleteResponseCommon.getUserDeleteResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userLogicalDeleteResponseCommon.getUserDeleteResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

}
