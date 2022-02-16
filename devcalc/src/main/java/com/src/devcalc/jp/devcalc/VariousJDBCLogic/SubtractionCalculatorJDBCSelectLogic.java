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
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalSubtractionJDBCSelectLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.SubtractionResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class SubtractionCalculatorJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public SubtractionCalculatorJDBCSelectLogic() {
		log = LogManager.getLogger(SubtractionCalculatorJDBCSelectLogic.this);
	}
	
	public Response F_SubtractionResponse(SubtractionResponseCommon subtractionResponseCommon) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		subtractionResponseDetails.setStatus(subtractionResponseCommon.getSubtractionResponseStatus());
		subtractionResponseDetails.setMessage(subtractionResponseCommon.getSubtractionResponseMessage());
		subtractionResponseDetails.setTime(LocalDateTime.now().toString());
		subtractionResponseDetails.setApino(subtractionResponseCommon.getSubtractionResponseAPINo());
		return Response.status(subtractionResponseCommon.getStatus()).entity(subtractionResponseDetails).build();
	}
	
	public Response F_SubtractionService(String userId) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		SubtractionResponseCommon subtractionResponseCommon = SubtractionResponseCommon.SubtractionS200;
		
		Response subtractionJDBCResponse = F_CheckRequestBody(userId);
		if(subtractionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionJDBCResponse;
		}else {
			log.info(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog1);
		}
		
		subtractionJDBCResponse = F_CheckUserIDSelect(userId);
		if(subtractionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionJDBCResponse;
		}else {
			log.info(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog2);
		}
		
		subtractionResponseDetails.setStatus(subtractionResponseCommon.getSubtractionResponseStatus());
		subtractionResponseDetails.setMessage(subtractionResponseCommon.getSubtractionResponseMessage());
		subtractionResponseDetails.setTime(LocalDateTime.now().toString());
		subtractionResponseDetails.setApino(subtractionResponseCommon.getSubtractionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId) {
		log.info(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog3);
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE400);
		}else {
			//Not Execute
		}
		log.info(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}

	private Response F_CheckUserIDSelect(String userId) {
		log.info(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog5);
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement.setString(1, userId);
				
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						//Not Execute
					}else {
						log.error(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog6);
						return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.fatal(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog7 + classNotFoundException);
					return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.error(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog8 + sqlException);
					return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
				}
			}
			break;
		}
		
		log.info(GlobalSubtractionJDBCSelectLogVariable.SubtractionSelectLog9);
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
}
