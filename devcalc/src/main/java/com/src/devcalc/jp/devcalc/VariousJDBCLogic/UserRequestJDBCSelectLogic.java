package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRequestResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserRequestJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserRequestJDBCSelectLogic() {
		log = LogManager.getLogger(UserRequestJDBCSelectLogic.this);
	}
	
	public Response F_UserRequestResponse(UserRequestResponseCommon userRequestResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userRequestResponseCommon.getUserRequestResponseStatus());
		generalResponseDetails.setMessage(userRequestResponseCommon.getUserRequestResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRequestResponseCommon.getUserRequestResponseAPINo());
		return Response.status(userRequestResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	public Response F_UserRequestJDBC(String userId, String request, String priority) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRequestResponseCommon userRequestResponseCommon = UserRequestResponseCommon.RequestS200;
		
		Response requestResponse = F_CheckRequestBody(userId, request, priority);
		if(requestResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return requestResponse;
		}else {
			//Not Execute
		}
		
		requestResponse = F_CheckUserInfoSelect(userId);
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
	
	private Response F_CheckUserInfoSelect(String userId) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement.setString(1, userId);
				
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						if(resultSet.getInt("C_DELETE_FLG") == 1) {
							return F_UserRequestResponse(UserRequestResponseCommon.RequestE401);
						}else if (resultSet.getInt("C_ADMINDELETE_FLG") == 1) {
							return F_UserRequestResponse(UserRequestResponseCommon.RequestE403);
						}else {
							//Not Execute
						}
					}else {
						return F_UserRequestResponse(UserRequestResponseCommon.RequestE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserRequestResponse(UserRequestResponseCommon.RequestE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserRequestResponse(UserRequestResponseCommon.RequestE500);
				}
			}
			break;
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
