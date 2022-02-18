package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserInquiryResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserInquiryJDBCInsertLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserInquiryJDBCInsertLogic() {
		log = LogManager.getLogger(UserInquiryJDBCInsertLogic.this);
	}
	
	public Response F_UserInquiryResponse(UserInquiryResponseCommon userInquiryResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userInquiryResponseCommon.getInquiryResponseStatus());
		generalResponseDetails.setMessage(userInquiryResponseCommon.getInquiryResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userInquiryResponseCommon.getInquiryResponseAPINo());
		return Response.status(userInquiryResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	public Response F_RegistJDBCService(String userId, String mail, String inquiry, String priority) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserInquiryResponseCommon userInquiryResponseCommon = UserInquiryResponseCommon.InquiryS200;
		
		Response inquiryResponse = F_CheckRequestBody(userId, mail, inquiry, priority);
		if(inquiryResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return inquiryResponse;
		}else {
			//Not Execute
		}
		
		inquiryResponse = F_RegistInsert(userId, mail, inquiry, priority);
		if(inquiryResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return inquiryResponse;
		}else {
			//Not Execute
		}
		
		generalResponseDetails.setStatus(userInquiryResponseCommon.getInquiryResponseStatus());
		generalResponseDetails.setMessage(userInquiryResponseCommon.getInquiryResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userInquiryResponseCommon.getInquiryResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId, String mail, String inquiry, String priority) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId) ||
			stringUtil.isNullorEmptytoString(mail) ||
			stringUtil.isNullorEmptytoString(inquiry) ||
			stringUtil.isNullorEmptytoString(priority)) {
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}

	private Response F_RegistInsert(String userId, String mail, String inquiry, String priority) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12))){
			connection.setAutoCommit(false);
			try(PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.UserInquirySQL)){
				preparedStatement.setString(1, userId);
				preparedStatement.setString(2, mail);
				preparedStatement.setString(3, inquiry);
				preparedStatement.setTimestamp(4, timestamp);
				preparedStatement.setString(5, priority);
				preparedStatement.executeUpdate();
				connection.commit();
			}
		} catch (ClassNotFoundException classNotFoundException) {
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE500);
		} catch (SQLException sqlException) {
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE500);
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
