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
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalMultiplicationJDBCSelectLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.MultiplicationResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class MultiplicationCalculatorJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public MultiplicationCalculatorJDBCSelectLogic() {
		log = LogManager.getLogger(MultiplicationCalculatorJDBCSelectLogic.this);
	}
	
	public Response F_MultiplicationResponse(MultiplicationResponseCommon multiplicationResponseCommon) {
		CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
		
		multiplicationResponseDetails.setStatus(multiplicationResponseCommon.getMultiplicationResponseStatus());
		multiplicationResponseDetails.setMessage(multiplicationResponseCommon.getMultiplicationResponseMessage());
		multiplicationResponseDetails.setTime(LocalDateTime.now().toString());
		multiplicationResponseDetails.setApino(multiplicationResponseCommon.getMultiplicationResponseAPINo());
		return Response.status(multiplicationResponseCommon.getStatus()).entity(multiplicationResponseDetails).build();
	}
	
	public Response F_MultiplicationService(String userId) {
		CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
		MultiplicationResponseCommon multiplicationResponseCommon = MultiplicationResponseCommon.MultiplicationS200;
		
		Response multiplicationJDBCResponse = F_CheckRequestBody(userId);
		if(multiplicationJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return multiplicationJDBCResponse;
		}else {
			log.info(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog1);
		}
		
		multiplicationJDBCResponse = F_CheckUserIDSelect(userId);
		if(multiplicationJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return multiplicationJDBCResponse;
		}else {
			log.info(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog2);
		}
		
		multiplicationResponseDetails.setStatus(multiplicationResponseCommon.getMultiplicationResponseStatus());
		multiplicationResponseDetails.setMessage(multiplicationResponseCommon.getMultiplicationResponseMessage());
		multiplicationResponseDetails.setTime(LocalDateTime.now().toString());
		multiplicationResponseDetails.setApino(multiplicationResponseCommon.getMultiplicationResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
	}

	private Response F_CheckRequestBody(String userId) {
		log.info(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog3);
		CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
	}
	
	private Response F_CheckUserIDSelect(String userId) {
		log.info(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog5);
		CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
						PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement.setString(1, userId);
				
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						//Not Execute
					}else {
						log.error(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog6);
						return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.fatal(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog7);
					return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.error(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog8);
					return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE500);
				}
			}
			break;
		}
		
		log.info(GlobalMultiplicationJDBCSelectLogVariable.MultiplicationJDBCSelectLog9);
		return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
	}
}
