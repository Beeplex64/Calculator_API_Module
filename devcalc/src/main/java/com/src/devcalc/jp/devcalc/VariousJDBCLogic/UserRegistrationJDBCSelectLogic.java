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
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRegistrationResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserRegistrationJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	//デフォルトコンストラクタ
	public UserRegistrationJDBCSelectLogic() {
		
	}
	
	public Response F_UserRegistResponse(UserRegistrationResponseCommon userRegistrationResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(userRegistrationResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	public Response F_UserRegistJDBC(String userId) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRegistrationResponseCommon userRegistrationResponseCommon = UserRegistrationResponseCommon.RegistS200;
		
		Response registResponse = F_CheckRequestBody(userId);
		if(registResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE400);
		}else {
			//Not Execute
		}
		
		registResponse = F_CheckUserInfoSelect(userId);
		if(registResponse.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE409);
		}else if(registResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
		}
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

	
	private Response F_CheckRequestBody(String userId) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE400);
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
						return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE409);
					}else {
						//Not Execute
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
				}
			}
			break;
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
