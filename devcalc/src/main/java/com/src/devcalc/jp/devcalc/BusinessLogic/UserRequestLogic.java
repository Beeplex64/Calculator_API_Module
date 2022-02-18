package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserRequestLogicLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRequestResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserRequestJDBCInsertLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserRequestJDBCSelectLogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserRequestLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//UserRequestJDBCSelectLogicクラスのインスタンス化
	UserRequestJDBCSelectLogic userRequestJDBCSelectLogic = new UserRequestJDBCSelectLogic();
	
	//UserRequestJDBCInsertLogicクラスのインスタンス化
	UserRequestJDBCInsertLogic userRequestJDBCInsertLogic = new UserRequestJDBCInsertLogic();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserRequestLogic() {
		log = LogManager.getLogger(UserRequestLogic.this);
	}

	public Response F_UserRequestResponse(UserRequestResponseCommon userRequestResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userRequestResponseCommon.getUserRequestResponseStatus());
		generalResponseDetails.setMessage(userRequestResponseCommon.getUserRequestResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRequestResponseCommon.getUserRequestResponseAPINo());
		return Response.status(userRequestResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	@Asynchronous
	public Future<Response> F_UserRequestService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRequestResponseCommon userRequestResponseCommon = UserRequestResponseCommon.RequestS200;
		
		generalResponseDetails.setStatus(userRequestResponseCommon.getUserRequestResponseStatus());
		generalResponseDetails.setMessage(userRequestResponseCommon.getUserRequestResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRequestResponseCommon.getUserRequestResponseAPINo());
		
		Response requestResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
		if(requestResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return new AsyncResult<Response>(requestResponse);
		}else {
			log.info(GlobalUserRequestLogicLogVariable.UserRequestLog1);
		}
		
		requestResponse = F_CheckUserInfoSelect(requestEntity, requestBodyEntity);
		if(requestResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return new AsyncResult<Response>(requestResponse);
		}else {
			log.info(GlobalUserRequestLogicLogVariable.UserRequestLog2);
		}
		
		requestResponse = F_InsertUserRequest(requestEntity, requestBodyEntity);
		if(requestResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return new AsyncResult<Response>(requestResponse);
		}else {
			log.info(GlobalUserRequestLogicLogVariable.UserRequestLog3);
		}
		
		return new AsyncResult<Response>(requestResponse);
	}
	
	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserRequestLogicLogVariable.UserRequestLog4);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getrequest()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getpriority())) {
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalUserRequestLogicLogVariable.UserRequestLog5);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckUserInfoSelect(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserRequestLogicLogVariable.UserRequestLog6);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String userId = new String();
		String request = new String();
		String priority = new String();
		
		userId = requestBodyEntity.getuserid();
		request = requestBodyEntity.getrequest();
		priority = requestBodyEntity.getpriority();
		Response CheckUserInfoResponse = userRequestJDBCSelectLogic.F_UserRequestJDBC(userId, request, priority);
		
		if(CheckUserInfoResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserRequestLogicLogVariable.UserRequestLog7);
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE400);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
			log.error(GlobalUserRequestLogicLogVariable.UserRequestLog8);
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE401);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
			log.error(GlobalUserRequestLogicLogVariable.UserRequestLog9);
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE403);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalUserRequestLogicLogVariable.UserRequestLog10);
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE500);
		}else {
			log.info(GlobalUserRequestLogicLogVariable.UserRequestLog11);
		}
		
		log.info(GlobalUserRequestLogicLogVariable.UserRequestLog12);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_InsertUserRequest(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserRequestLogicLogVariable.UserRequestLog13);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String userId = new String();
		String request = new String();
		String priority = new String();
		
		userId = requestBodyEntity.getuserid();
		request = requestBodyEntity.getrequest();
		priority = requestBodyEntity.getpriority();
		Response InsertUserRequestResponse = userRequestJDBCInsertLogic.F_RequestJDBCService(userId, request, priority);
		
		if(InsertUserRequestResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserRequestLogicLogVariable.UserRequestLog14);
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE400);
		}else if(InsertUserRequestResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
			log.error(GlobalUserRequestLogicLogVariable.UserRequestLog15);
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE500);
		}else {
			log.info(GlobalUserRequestLogicLogVariable.UserRequestLog16);
		}
		
		log.info(GlobalUserRequestLogicLogVariable.UserRequestLog17);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
