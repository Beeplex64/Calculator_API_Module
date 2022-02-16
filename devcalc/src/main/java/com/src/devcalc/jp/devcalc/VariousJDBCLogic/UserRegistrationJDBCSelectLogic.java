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
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserRegistrationJDBCSelectLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserRegistrationResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserRegistrationJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserRegistrationJDBCSelectLogic() {
		log = LogManager.getLogger(UserRegistrationJDBCSelectLogic.this);
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
		if(registResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return registResponse;
		}else {
			log.info(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog1);
		}
		
		registResponse = F_CheckUserInfoSelect(userId);
		if(registResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return registResponse;
		}else {
			log.info(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog2);
		}
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

	
	private Response F_CheckRequestBody(String userId) {
		log.info(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog3);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckUserInfoSelect(String userId) {
		log.info(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog5);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement.setString(1, userId);
				
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						log.error(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog6);
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
					log.fatal(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog7 + classNotFoundException);
					return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.error(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog8 + sqlException);
					return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
				}
			}
			break;
		}
		
		log.info(GlobalUserRegistrationJDBCSelectLogVariable.RegistrationJDBCSelectLog9);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
