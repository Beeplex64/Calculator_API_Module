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
import com.src.devcalc.jp.devcalc.ResponseContent.UserInquiryResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserInquiryJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserInquiryJDBCSelectLogic() {
		log = LogManager.getLogger(UserInquiryJDBCSelectLogic.this);
	}
	
	public Response F_UserInquiryResponse(UserInquiryResponseCommon userInquiryResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userInquiryResponseCommon.getInquiryResponseStatus());
		generalResponseDetails.setMessage(userInquiryResponseCommon.getInquiryResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userInquiryResponseCommon.getInquiryResponseAPINo());
		return Response.status(userInquiryResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}

	public Response F_UserInquiryJDBC(String userId) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserInquiryResponseCommon userInquiryResponseCommon = UserInquiryResponseCommon.InquiryS200;
		
		Response inquiryResponse = F_CheckRequestBody(userId);
		if(inquiryResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return inquiryResponse;
		}else {
			//Not Execute
		}
		
		inquiryResponse = F_CheckUserInfoSelect(userId);
		if(inquiryResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return inquiryResponse;
		}else {
			//Not Execute
		}
		
		generalResponseDetails.setStatus(userInquiryResponseCommon.getInquiryResponseStatus());
		generalResponseDetails.setMessage(userInquiryResponseCommon.getInquiryResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userInquiryResponseCommon.getInquiryResponseAPINo());
		return Response.status(userInquiryResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)){
			return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE400);
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
							return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE403);
						}else if(resultSet.getInt("C_ADMINDELETE_FLG") == 1) {
							return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE403);
						}else {
							//Not Execute
						}
					}else {
						return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_UserInquiryResponse(UserInquiryResponseCommon.InquiryE500);
				}
			}
			break;
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
