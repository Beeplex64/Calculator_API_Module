package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.BusinessLogic.UserLogicalDeleteLogic;
import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalUserLogicalDeleteJDBCUpdateVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.UserLogicalDeleteResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class UserLogicalDeleteJDBCUpdateLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public UserLogicalDeleteJDBCUpdateLogic() {
		log = LogManager.getLogger(UserLogicalDeleteJDBCUpdateLogic.this);
	}

	public Response F_UserDeleteResponse(UserLogicalDeleteResponseCommon userLogicalDeleteResponseCommon) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		generalResponseDetails.setStatus(userLogicalDeleteResponseCommon.getUserDeleteResponseStatus());
		generalResponseDetails.setMessage(userLogicalDeleteResponseCommon.getUserDeleteResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userLogicalDeleteResponseCommon.getUserDeleteResponseAPINo());
		return Response.status(userLogicalDeleteResponseCommon.getStatus()).entity(generalResponseDetails).build();
	}
	
	public Response F_UserDeleteJDBC(String userId) {
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		UserLogicalDeleteResponseCommon userLogicalDeleteResponseCommon = UserLogicalDeleteResponseCommon.DeleteS200;
		
		Response deleteResponse = F_CheckRequestBody(userId);
		if(deleteResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return deleteResponse;
		}else {
			log.info(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog1);
		}
		
		deleteResponse = F_ExecuteDelete(userId);
		if(deleteResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return deleteResponse;
		}else {
			log.info(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog2);
		}
		
		generalResponseDetails.setStatus(userLogicalDeleteResponseCommon.getUserDeleteResponseStatus());
		generalResponseDetails.setMessage(userLogicalDeleteResponseCommon.getUserDeleteResponseMessage());
		generalResponseDetails.setTime(LocalDateTime.now().toString());
		generalResponseDetails.setApino(userLogicalDeleteResponseCommon.getUserDeleteResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId) {
		log.info(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog3);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_UserDeleteResponse(UserLogicalDeleteResponseCommon.DeleteE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
	
	private Response F_ExecuteDelete(String userId) {
		log.info(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog5);
		GeneralResponseDetails generalResponseDetails = new GeneralResponseDetails();
		
		Timestamp deleteTime = new Timestamp(System.currentTimeMillis());
		int retryMaxCount = 0;
		int delete_FLG = 1;
		
		while(retryMaxCount < 3) {
			try(Connection connection1 = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement1 = connection1.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement1.setString(1, userId);
				
				try(ResultSet resultSet = preparedStatement1.executeQuery()){
					if(resultSet.next()) {
						try(Connection connection2 = dataSourceHolder.connection(Duration.ofSeconds(12))){
							connection2.setAutoCommit(false);
							try(PreparedStatement preparedStatement2 = connection2.prepareStatement(GlobalLogicVariable.UserDeleteSQL)){
								preparedStatement2.setInt(1, delete_FLG);
								preparedStatement2.setTimestamp(2, deleteTime);
								preparedStatement2.setString(3, userId);
								preparedStatement2.executeUpdate();
								connection2.commit();
							}
						}
					}else {
						//Not Execute
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.fatal(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog6);
					return F_UserDeleteResponse(UserLogicalDeleteResponseCommon.DeleteE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.error(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog7);
					return F_UserDeleteResponse(UserLogicalDeleteResponseCommon.DeleteE500);
				}
			}
			break;
		}
		
		log.info(GlobalUserLogicalDeleteJDBCUpdateVariable.DeleteJDBCUpdateLog8);
		return Response.status(Response.Status.OK.getStatusCode()).entity(generalResponseDetails).build();
	}
}
