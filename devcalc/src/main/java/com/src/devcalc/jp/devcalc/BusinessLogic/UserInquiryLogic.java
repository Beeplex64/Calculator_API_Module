package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserInquiryLogicLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserInquiryResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserInquiryJDBCInsertLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserInquiryJDBCSelectLogic;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserInquiryLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//UserInquiryJDBCSelectLogicクラスのインスタンス化
	UserInquiryJDBCSelectLogic userInquiryJDBCSelectLogic = new UserInquiryJDBCSelectLogic();
	
	//UserInquiryJDBCInsertLogicクラスのインスタンス化
	UserInquiryJDBCInsertLogic userInquiryJDBCInsertLogic = new UserInquiryJDBCInsertLogic();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserInquiryLogic() {
		log = LogManager.getLogger(UserInquiryLogic.this);
	}
	
	public Response F_UserInquiryResponse(UserInquiryResponseCommon userInquiryResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userInquiryResponseCommon.getInquiryResponseStatus());
		generalResponseDetails.setMessage(userInquiryResponseCommon.getInquiryResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userInquiryResponseCommon.getInquiryResponseAPINo());
		return Response.status(userInquiryResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}

	@Asynchronous
	public Future<Response> F_UserInquiryService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity){
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserInquiryResponseCommon userInquiryResponseCommon = UserInquiryResponseCommon.InquiryS200;
		
		generalResponseDetails.setStatus(userInquiryResponseCommon.getInquiryResponseStatus());
		generalResponseDetails.setMessage(userInquiryResponseCommon.getInquiryResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userInquiryResponseCommon.getInquiryResponseAPINo());
		
		Response inquiryResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
		if(inquiryResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return new AsyncResult<Response>(inquiryResponse);
		}else {
			log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog1);
		}
		
		inquiryResponse = F_CheckUserInfo(requestEntity, requestBodyEntity);
		if(inquiryResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return new AsyncResult<Response>(inquiryResponse);
		}else {
			log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog2);
		}
		
		inquiryResponse = F_InsertInquiry(requestEntity, requestBodyEntity);
		if(inquiryResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return new AsyncResult<Response>(inquiryResponse);
		}else {
			log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog3);
		}
		
		return new AsyncResult<Response>(inquiryResponse);
	}
	
	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog4);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getmail()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getinquiry()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getpriority())) {
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog5);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckUserInfo(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog6);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String userId = new String();
		
		userId = requestBodyEntity.getuserid();
		Response CheckUserInfoResponse = userInquiryJDBCSelectLogic.F_UserInquiryJDBC(userId);
		
		if(CheckUserInfoResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserInquiryLogicLogVariable.UserInquiryLog7);
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE400);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
			log.error(GlobalUserInquiryLogicLogVariable.UserInquiryLog8);
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE403);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalUserInquiryLogicLogVariable.UserInquiryLog9);
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE500);
		}else {
			log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog10);
		}
		
		log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog11);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_InsertInquiry(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog12);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String userId = new String();
		String mail = new String();
		String inquiry = new String();
		String priority = new String();
		
		userId = requestBodyEntity.getuserid();
		mail = requestBodyEntity.getmail();
		inquiry = requestBodyEntity.getinquiry();
		priority = requestBodyEntity.getpriority();
		
		Response InquiryInsertResonse = userInquiryJDBCInsertLogic.F_RegistJDBCService(userId, mail, inquiry, priority);
		
		if(InquiryInsertResonse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserInquiryLogicLogVariable.UserInquiryLog13);
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE400);
		}else if(InquiryInsertResonse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalUserInquiryLogicLogVariable.UserInquiryLog14);
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE500);
		}else {
			log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog15);
		}
		
		log.info(GlobalUserInquiryLogicLogVariable.UserInquiryLog16);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
