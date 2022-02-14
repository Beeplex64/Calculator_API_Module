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
import com.src.devcalc.jp.devcalc.ResponseContent.UserRegistrationResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserRegistrationJDBCInsertLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	//デフォルトコンストラクタ
	public UserRegistrationJDBCInsertLogic() {
		
	}
	
	public Response F_UserRegistResponse(UserRegistrationResponseCommon userRegistrationResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(userRegistrationResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}

	public Response F_RegistJDBCService(String userId, String password, String phone, String profession, String mail, int age) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserRegistrationResponseCommon userRegistrationResponseCommon = UserRegistrationResponseCommon.RegistS200;
		
		Response registResponse = F_CheckRequestBody(userId, password, phone, profession, mail);
		if(registResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return registResponse;
		}else {
			//Not Execute
		}
		
		registResponse = F_RegistInsert(userId, password, phone, profession, mail, age);
		if(registResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return registResponse;
		}else {
			//Not Execute
		}
		
		generalResponseDetails.setStatus(userRegistrationResponseCommon.getRegistResponseStatus());
		generalResponseDetails.setMessage(userRegistrationResponseCommon.getRegistResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userRegistrationResponseCommon.getRegistResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId, String password, String phone, String profession, String mail) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId) ||
			stringUtil.isNullorEmptytoString(password) ||
			stringUtil.isNullorEmptytoString(phone) ||
			stringUtil.isNullorEmptytoString(profession) ||
			stringUtil.isNullorEmptytoString(mail)) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_RegistInsert(String userId, String password, String phone, String profession, String mail, int age) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int logicalDelete_flg = 0;
		int adminDelete_flg = 0;
		String jwtToken = "initToken";
		
		try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12))){
			connection.setAutoCommit(false);
			try(PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.RegistInsertSQL)){
				preparedStatement.setString(1, userId);
				preparedStatement.setString(2, phone);
				preparedStatement.setString(3, profession);
				preparedStatement.setInt(4, age);
				preparedStatement.setInt(5, logicalDelete_flg);
				preparedStatement.setTimestamp(6, timestamp);
				preparedStatement.setTimestamp(7, timestamp);
				preparedStatement.setString(8, password);
				preparedStatement.setTimestamp(9, timestamp);
				preparedStatement.setTimestamp(10, timestamp);
				preparedStatement.setString(11, jwtToken);
				preparedStatement.setInt(12, adminDelete_flg);
				preparedStatement.setString(13, mail);
				preparedStatement.executeUpdate();
				connection.commit();
			}
		} catch (ClassNotFoundException classNotFoundException) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
		} catch (SQLException sqlException) {
			return F_UserRegistResponse(UserRegistrationResponseCommon.RegistE500);
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
