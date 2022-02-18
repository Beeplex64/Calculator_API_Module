package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRequestResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequestScoped
public class UserRequestJDBCInsertLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserRequestJDBCInsertLogic() {
		log = LogManager.getLogger(UserRequestJDBCInsertLogic.this);
	}
	
	public Response F_UserRequestResponse(UserRequestResponseCommon userRequestResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userRequestResponseCommon.getUserRequestResponseStatus());
		generalResponseDetails.setMessage(userRequestResponseCommon.getUserRequestResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRequestResponseCommon.getUserRequestResponseAPINo());
		return Response.status(userRequestResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	public Response F_RequestJDBCService(String userId, String request, String priority){
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRequestResponseCommon userRequestResponseCommon = UserRequestResponseCommon.RequestS200;
		
		Response requestResponse = F_CheckRequestBody(userId, request, priority);
		if(requestResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return requestResponse;
		}else {
			//Not Execute
		}
		
		requestResponse = F_RegistInsert(userId, request, priority);
		if(requestResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return requestResponse;
		}else {
			//Not Execute
		}
		
		generalResponseDetails.setStatus(userRequestResponseCommon.getUserRequestResponseStatus());
		generalResponseDetails.setMessage(userRequestResponseCommon.getUserRequestResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRequestResponseCommon.getUserRequestResponseAPINo());
		return Response.status(userRequestResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId, String request, String priority) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId) ||
			stringUtil.isNullorEmptytoString(request) ||
			stringUtil.isNullorEmptytoString(priority)) {
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_RegistInsert(String userId, String request, String priority) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12))){
			connection.setAutoCommit(false);
			try(PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.UserRequestSQL)){
				preparedStatement.setString(1, userId);
				preparedStatement.setString(2, request);
				preparedStatement.setTimestamp(3, timestamp);
				preparedStatement.setString(4, priority);
				preparedStatement.executeUpdate();
				connection.commit();
			}
		} catch (ClassNotFoundException classNotFoundException) {
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE500);
		} catch (SQLException sqlException) {
			return F_UserRequestResponse(UserRequestResponseCommon.RequestE500);
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
