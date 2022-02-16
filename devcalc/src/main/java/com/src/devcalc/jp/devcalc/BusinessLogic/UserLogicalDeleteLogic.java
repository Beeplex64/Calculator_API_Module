package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserLogicalDeleteLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLogicalDeleteResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.SubtractionCalculatorJDBCSelectLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.UserLogicalDeleteJDBCUpdateLogic;

@RequestScoped
public class UserLogicalDeleteLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//UserLogicalDeleteJDBCUpdateLogicクラスのインスタンス化
	UserLogicalDeleteJDBCUpdateLogic userLogicalDeleteJDBCUpdateLogic = new UserLogicalDeleteJDBCUpdateLogic();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserLogicalDeleteLogic() {
		log = LogManager.getLogger(UserLogicalDeleteLogic.this);
	}
	
	public Response F_UserDeleteResponse(UserLogicalDeleteResponseCommon userLogicalDeleteResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userLogicalDeleteResponseCommon.getUserDeleteResponseStatus());
		generalResponseDetails.setMessage(userLogicalDeleteResponseCommon.getUserDeleteResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userLogicalDeleteResponseCommon.getUserDeleteResponseAPINo());
		return Response.status(userLogicalDeleteResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	public Response F_UserDeleteService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserLogicalDeleteResponseCommon userLogicalDeleteResponseCommon = UserLogicalDeleteResponseCommon.DeleteS200;
		
		Response deleteResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
		if(deleteResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return deleteResponse;
		}else {
			log.info(GlobalUserLogicalDeleteLogVariable.UserDeleteLog1);
		}
		
		deleteResponse = F_ExecuteUserDelete(requestBodyEntity);
		if(deleteResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return deleteResponse;
		}else {
			log.info(GlobalUserLogicalDeleteLogVariable.UserDeleteLog2);
		}
		
		generalResponseDetails.setStatus(userLogicalDeleteResponseCommon.getUserDeleteResponseStatus());
		generalResponseDetails.setMessage(userLogicalDeleteResponseCommon.getUserDeleteResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userLogicalDeleteResponseCommon.getUserDeleteResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserLogicalDeleteLogVariable.UserDeleteLog3);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid())) {
			return F_UserDeleteResponse(UserLogicalDeleteResponseCommon.DeleteE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalUserLogicalDeleteLogVariable.UserDeleteLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_ExecuteUserDelete(RequestBodyEntity requestBodyEntity) {
		log.info(GlobalUserLogicalDeleteLogVariable.UserDeleteLog5);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		String userId = new String();
		
		userId = requestBodyEntity.getuserid();
		Response ExecuteUserDeleteResponse = userLogicalDeleteJDBCUpdateLogic.F_UserDeleteJDBC(userId);
		
		if(ExecuteUserDeleteResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalUserLogicalDeleteLogVariable.UserDeleteLog6);
			return F_UserDeleteResponse(UserLogicalDeleteResponseCommon.DeleteE400);
		}else if(ExecuteUserDeleteResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalUserLogicalDeleteLogVariable.UserDeleteLog7);
			return F_UserDeleteResponse(UserLogicalDeleteResponseCommon.DeleteE500);
		}else {
			log.info(GlobalUserLogicalDeleteLogVariable.UserDeleteLog8);
		}
		
		log.info(GlobalUserLogicalDeleteLogVariable.UserDeleteLog9);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

}
