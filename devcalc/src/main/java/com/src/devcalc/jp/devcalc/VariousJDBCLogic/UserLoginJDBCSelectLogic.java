package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLoginResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLoginResponseDetails;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserLoginJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	//デフォルトコンストラクタ
	public UserLoginJDBCSelectLogic() {
		
	}

	public Response F_UserLoginResponse(UserLoginResponseCommon userLoginResponseCommon) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		userLoginResponseDetails.setStatus(userLoginResponseCommon.getUserLoginResponseStatus());
		userLoginResponseDetails.setMessage(userLoginResponseCommon.getUserLoginResponseMessage());
		userLoginResponseDetails.setTime(LocalDateTime.now().toString());
		userLoginResponseDetails.setApino(userLoginResponseCommon.getUserLoginResponseAPINo());
		return Response.status(userLoginResponseCommon.getStatus()).entity(userLoginResponseDetails).build();
	}
	
	public Response F_UserLoginJDBC(String userId, String password) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		UserLoginResponseCommon userLoginResponseCommon = UserLoginResponseCommon.LoginS200;
		
		Response loginResponse = F_CheckRequestBody(userId, password);
		if(loginResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return loginResponse;
		}else {
			//Not Execute
		}
		loginResponse = F_CheckUserInfoSelect(userId, password);
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
	
	private Response F_CheckRequestBody(String userId, String password) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		if(stringUtil.isNullorEmptytoString(userId) ||
			stringUtil.isNullorEmptytoString(password)) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CheckUserInfoSelect(String userId, String password) {
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement.setString(1,  userId);
				
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						if(resultSet.getInt("C_DELETE_FLG") == 1) {
							return F_UserLoginResponse(UserLoginResponseCommon.LoginE401);
						}else if(resultSet.getString("C_PASSWORD").equals(password)) {
							return F_UserLoginResponse(UserLoginResponseCommon.LoginE404);
						}else {
							//Not Execute
						}
					}else {
						return F_UserLoginResponse(UserLoginResponseCommon.LoginE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
				}
			}
			break;
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
}
