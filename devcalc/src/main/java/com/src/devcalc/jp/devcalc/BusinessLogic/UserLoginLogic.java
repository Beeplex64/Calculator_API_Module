package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GenerateVerifyJWT.GenerateAndVerifyJWTToken;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserLoginLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLoginResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLoginResponseDetails;
import com.src.devcalc.jp.devcalc.SHA256bitHashUtil.SHA256bitHashUtil;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserLoginJDBCSelectLogic;

@RequestScoped
public class UserLoginLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//UserLoginJDBCSelectLogicクラスのインスタンス化
	UserLoginJDBCSelectLogic userLoginJDBCSelectLogic = new UserLoginJDBCSelectLogic();
	
	//GenerateAndVerifyJWTTokenクラスのインスタンス化
	GenerateAndVerifyJWTToken generateAndVerifyJWTToken = new GenerateAndVerifyJWTToken();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserLoginLogic() {
		log = LogManager.getLogger(UserLoginLogic.this);
	}
	
	public Response F_UserLoginResponse(UserLoginResponseCommon userLoginResponseCommon) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		userLoginResponseDetails.setStatus(userLoginResponseCommon.getUserLoginResponseStatus());
		userLoginResponseDetails.setMessage(userLoginResponseCommon.getUserLoginResponseMessage());
		userLoginResponseDetails.setTime(LocalDateTime.now().toString());
		userLoginResponseDetails.setApino(userLoginResponseCommon.getUserLoginResponseAPINo());
		return Response.status(userLoginResponseCommon.getStatus()).entity(userLoginResponseDetails).build();
	}
	
	public Response F_UserLoginService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		UserLoginResponseCommon userLoginResponseCommon = UserLoginResponseCommon.LoginS200;
		StringBuilder hashedPassword = new StringBuilder();
		
		Response loginResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
		if(loginResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return loginResponse;
		}else {
			log.info(GlobalUserLoginLogVariable.UserLoginLog1);
		}
		
		loginResponse = F_PasswordHash(requestBodyEntity, hashedPassword);
		
		loginResponse = F_CheckUserInfoSelect(requestBodyEntity, hashedPassword);
		if(loginResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return loginResponse;
		}else {
			log.info(GlobalUserLoginLogVariable.UserLoginLog2);
		}
		
		loginResponse = F_CallJWTLogic(requestBodyEntity);
		if(loginResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return loginResponse;
		}else {
			log.info(GlobalUserLoginLogVariable.UserLoginLog3);
		}
		
		userLoginResponseDetails.setStatus(userLoginResponseCommon.getUserLoginResponseStatus());
		userLoginResponseDetails.setMessage(userLoginResponseCommon.getUserLoginResponseMessage());
		userLoginResponseDetails.setTime(LocalDateTime.now().toString());
		userLoginResponseDetails.setApino(userLoginResponseCommon.getUserLoginResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserLoginLogVariable.UserLoginLog4);
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid()) || 
			stringUtil.isNullorEmptytoString(requestBodyEntity.getpassword())) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else {
			//Not Execute
		}
		log.info(GlobalUserLoginLogVariable.UserLoginLog5);
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}

	private Response F_PasswordHash(RequestBodyEntity requestBodyEntity, StringBuilder hashedPassword) {
		log.info(GlobalUserLoginLogVariable.UserLoginLog6);
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		String SHA256Password = SHA256bitHashUtil.getHashInsertDBString("password", requestBodyEntity.getpassword());
		hashedPassword.append(SHA256Password);
		
		log.info(GlobalUserLoginLogVariable.UserLoginLog7);
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CheckUserInfoSelect(RequestBodyEntity requestBodyEntity, StringBuilder hashedPassword) {
		log.info(GlobalUserLoginLogVariable.UserLoginLog8);
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		String userId = new String();
		String password = new String();
		
		userId = requestBodyEntity.getuserid();
		password = hashedPassword.toString();
		Response CheckUserInfoResponse = userLoginJDBCSelectLogic.F_UserLoginJDBC(userId, password);
		
		if(CheckUserInfoResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserLoginLogVariable.UserLoginLog9);
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
			log.error(GlobalUserLoginLogVariable.UserLoginLog10);
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE401);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			log.error(GlobalUserLoginLogVariable.UserLoginLog11);
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE404);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalUserLoginLogVariable.UserLoginLog12);
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
		}else {
			log.info(GlobalUserLoginLogVariable.UserLoginLog13);
		}
		log.info(GlobalUserLoginLogVariable.UserLoginLog14);
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CallJWTLogic(RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserLoginLogVariable.UserLoginLog15);
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		String userId = new String();
		
		userId = requestBodyEntity.getuserid();
		Response jwtLoginToken = generateAndVerifyJWTToken.F_GenerateJWTService(userId);
		
		if(jwtLoginToken.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserLoginLogVariable.UserLoginLog16);
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else if(jwtLoginToken.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
			log.error(GlobalUserLoginLogVariable.UserLoginLog17);
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
		}else{
			log.info(GlobalUserLoginLogVariable.UserLoginLog18);
		}
		
		log.info(GlobalUserLoginLogVariable.UserLoginLog19);
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
}
