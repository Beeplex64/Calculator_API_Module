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
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserLoginJDBCSelectLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLoginResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLoginResponseDetails;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserLoginJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserLoginJDBCSelectLogic() {
		log = LogManager.getLogger(UserLoginJDBCSelectLogic.this);
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
			log.info(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog1);
		}
		loginResponse = F_CheckUserInfoSelect(userId, password);
		if(loginResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return loginResponse;
		}else {
			log.info(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog2);
		}
		
		userLoginResponseDetails.setStatus(userLoginResponseCommon.getUserLoginResponseStatus());
		userLoginResponseDetails.setMessage(userLoginResponseCommon.getUserLoginResponseMessage());
		userLoginResponseDetails.setTime(LocalDateTime.now().toString());
		userLoginResponseDetails.setApino(userLoginResponseCommon.getUserLoginResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId, String password) {
		log.info(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog3);
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		if(stringUtil.isNullorEmptytoString(userId) ||
			stringUtil.isNullorEmptytoString(password)) {
			return F_UserLoginResponse(UserLoginResponseCommon.LoginE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
	
	private Response F_CheckUserInfoSelect(String userId, String password) {
		log.info(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog5);
		UserLoginResponseDetails userLoginResponseDetails = new UserLoginResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement.setString(1,  userId);
				
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						if(resultSet.getInt("C_DELETE_FLG") == 1) {
							log.error(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog6);
							return F_UserLoginResponse(UserLoginResponseCommon.LoginE401);
						}else if(resultSet.getString("C_PASSWORD").equals(password)) {
							log.error(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog7);
							return F_UserLoginResponse(UserLoginResponseCommon.LoginE404);
						}else {
							//Not Execute
						}
					}else {
						log.error(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog7);
						return F_UserLoginResponse(UserLoginResponseCommon.LoginE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.fatal(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog8 + classNotFoundException);
					return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.error(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog9 + sqlException);
					return F_UserLoginResponse(UserLoginResponseCommon.LoginE500);
				}
			}
			break;
		}
		
		log.info(GlobalUserLoginJDBCSelectLogVariable.LoginJDBCSelectLog10);
		return Response.status(Response.Status.OK.getStatusCode()).entity(userLoginResponseDetails).build();
	}
}
