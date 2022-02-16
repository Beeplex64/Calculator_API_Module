package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserRegistrationLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRegistrationResponseCommon;
import com.src.devcalc.jp.devcalc.SHA256bitHashUtil.SHA256bitHashUtil;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserRegistrationJDBCInsertLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserRegistrationJDBCSelectLogic;

@RequestScoped
public class UserRegistretionLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//UserRegistrationJDBCSelectLogicクラスのインスタンス化
	UserRegistrationJDBCSelectLogic userRegistrationJDBCSelectLogic = new UserRegistrationJDBCSelectLogic();
	
	//UserRegistrationJDBCInsertLogicクラスのインスタンス化
	UserRegistrationJDBCInsertLogic userRegistrationJDBCInsertLogic = new UserRegistrationJDBCInsertLogic();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserRegistretionLogic() {
		log = LogManager.getLogger(UserRegistretionLogic.this);
	}
	
	public Response F_UserRegistResponse(UserRegistrationResponseCommon userRegistrationResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(userRegistrationResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}

	public Response F_RegistService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRegistrationResponseCommon userRegistrationResponseCommon = UserRegistrationResponseCommon.RegistS200;
		StringBuilder hashedPassword = new StringBuilder();
		StringBuilder hashedPhone = new StringBuilder();
		StringBuilder hashedMail = new StringBuilder();
		
		Response registResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
		if(registResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return registResponse;
		}else {
			log.info(GlobalUserRegistrationLogVariable.UserRegistLog1);
		}
		
		registResponse = F_PasswordHash(requestBodyEntity, hashedPassword);
		
		registResponse = F_PhoneHash(requestBodyEntity, hashedPhone);
		
		registResponse = F_MailHash(requestBodyEntity, hashedMail);
		
		registResponse = F_CheckUserInfoSelect(requestBodyEntity);
		if(registResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return registResponse;
		}else {
			log.info(GlobalUserRegistrationLogVariable.UserRegistLog2);
		}
		
		registResponse = F_RegistUserInfoInsert(requestBodyEntity, hashedPassword, hashedPhone, hashedMail);
		if(registResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return registResponse;
		}else {
			log.info(GlobalUserRegistrationLogVariable.UserRegistLog3);
		}
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog4);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getpassword()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getprofession()) ||
			stringUtil.isNullorEmptytoString(requestBodyEntity.getmail())) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE400);
		}else {
			//Not Execute
		}
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog5);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_PasswordHash(RequestBodyEntity requestBodyEntity, StringBuilder hashedPassword) {
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog6);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String SHA256Password = SHA256bitHashUtil.getHashInsertDBString("password", requestBodyEntity.getpassword());
		hashedPassword.append(SHA256Password);
		
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog7);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_PhoneHash(RequestBodyEntity requestBodyEntity, StringBuilder hashedPhone) {
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog8);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String phoneNumber = String.valueOf(requestBodyEntity.getphone());
		String SHA256Phone = SHA256bitHashUtil.getHashInsertDBString("password", phoneNumber);
		hashedPhone.append(SHA256Phone);
		
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog9);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_MailHash(RequestBodyEntity requestBodyEntity, StringBuilder hashedMail) {
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog10);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String SHA256Mail = SHA256bitHashUtil.getHashInsertDBString("mail", requestBodyEntity.getmail());
		hashedMail.append(SHA256Mail);
		
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog11);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckUserInfoSelect(RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog12);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String userId = new String();
		
		userId = requestBodyEntity.getuserid();
		Response CheckUserInfoResponse = userRegistrationJDBCSelectLogic.F_UserRegistJDBC(userId);
		
		if(CheckUserInfoResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserRegistrationLogVariable.UserRegistLog13);
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE400);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
			log.error(GlobalUserRegistrationLogVariable.UserRegistLog14);
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE409);
		}else if(CheckUserInfoResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalUserRegistrationLogVariable.UserRegistLog15);
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
		}else {
			log.error(GlobalUserRegistrationLogVariable.UserRegistLog16);
		}
		
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_RegistUserInfoInsert(RequestBodyEntity requestBodyEntity, StringBuilder hashedPassword, StringBuilder hashedPhone, StringBuilder hashedMail) {
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog17);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String userId = new String();
		String password = new String();
		String phone = new String();
		String profession = new String();
		String mail = new String();
		int age = 0;
		
		userId = requestBodyEntity.getuserid();
		password = hashedPassword.toString();
		phone = hashedPhone.toString();
		profession = requestBodyEntity.getprofession();
		mail = hashedMail.toString();
		age = requestBodyEntity.getage();
		Response RegistUserInfoResponse = userRegistrationJDBCInsertLogic.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		
		if(RegistUserInfoResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserRegistrationLogVariable.UserRegistLog18);
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE400);
		}else if(RegistUserInfoResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalUserRegistrationLogVariable.UserRegistLog19);
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
		}else {
			log.info(GlobalUserRegistrationLogVariable.UserRegistLog20);
		}
		
		log.info(GlobalUserRegistrationLogVariable.UserRegistLog21);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
