package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GenerateVerifyJWT.GenerateAndVerifyJWTToken;
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
	
	//デフォルトコンストラクタ
	public UserLoginLogic() {
		
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
			//Not Execute
		}
		
		loginResponse = F_PasswordHash(requestBodyEntity, hashedPassword);
		
		loginResponse = F_CheckUserInfoSelect(requestBodyEntity, hashedPassword);
		if(loginResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return loginResponse;
		}else {
			//Not Execute
		}
		
		loginResponse = F_CallJWTLogic(requestBodyEntity);
		if(loginResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return loginResponse;
		}else {
			//Not Execute
		}
		
		userLoginResponseDetails.setStatus(userLoginResponseCommon.getUserLoginResponseStatus());
		userLoginResponseDetails.setMessage(userLoginResponseCommon.getUserLoginResponseMessage());
		userLoginResponseDetails.setTime(LocalDateTime.now().toString());
		userLoginResponseDetails.setApino(userLoginResponseCommon.getUserLoginResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid()) || 
			stringUtil.isNullorEmptytoString(requestBodyEntity.getpassword())) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}

	private Response F_PasswordHash(RequestBodyEntity requestBodyEntity, StringBuilder hashedPassword) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		String SHA256Password = SHA256bitHashUtil.getHashInsertDBString("password", requestBodyEntity.getpassword());
		hashedPassword.append(SHA256Password);
		
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CheckUserInfoSelect(RequestBodyEntity requestBodyEntity, StringBuilder hashedPassword) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		String userId = new String();
		String password = new String();
		
		userId = requestBodyEntity.getuserid();
		password = hashedPassword.toString();
		Response CheckUserInfoResponse = userLoginJDBCSelectLogic.F_UserLoginJDBC(userId, password);
		
		if(CheckUserInfoResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE401);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE404);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CallJWTLogic(RequestBodyEntity requestBodyEntity) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		String userId = new String();
		
		userId = requestBodyEntity.getuserid();
		Response jwtLoginToken = generateAndVerifyJWTToken.F_GenerateJWTService(userId);
		
		if(jwtLoginToken.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else if(jwtLoginToken.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
		}else{
			//Not Execute
		}
		
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
}
