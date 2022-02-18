package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalSubtractionJDBCInsertLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.SubtractionResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class SubtractionCalculatorJDBCInsertLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public SubtractionCalculatorJDBCInsertLogic() {
		log = LogManager.getLogger(SubtractionCalculatorJDBCInsertLogic.this);
	}

	public Response F_SubtractionResponse(SubtractionResponseCommon subtractionResponseCommon) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		subtractionResponseDetails.setStatus(subtractionResponseCommon.getSubtractionResponseStatus());
		subtractionResponseDetails.setMessage(subtractionResponseCommon.getSubtractionResponseMessage());
		subtractionResponseDetails.setTime(LocalDateTime.now().toString());
		subtractionResponseDetails.setApino(subtractionResponseCommon.getSubtractionResponseAPINo());
		return Response.status(subtractionResponseCommon.getStatus()).entity(subtractionResponseDetails).build();
	}
	
	public Response F_SubtractionJDBCService(String userId, Integer number1, Integer number2, String symbol, Integer result) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		SubtractionResponseCommon subtractionResponseCommon = SubtractionResponseCommon.SubtractionS200;
		
		Response subtractionJDBCResponse = F_CheckRequestBody(userId, symbol);
		if(subtractionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionJDBCResponse;
		}else {
			log.info(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog1);
		}
		
		subtractionJDBCResponse = F_ResultInsert(userId, number1, number2, symbol, result);
		if(subtractionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionJDBCResponse;
		}else {
			log.info(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog2);
		}
		
		subtractionResponseDetails.setStatus(subtractionResponseCommon.getSubtractionResponseStatus());
		subtractionResponseDetails.setMessage(subtractionResponseCommon.getSubtractionResponseMessage());
		subtractionResponseDetails.setTime(LocalDateTime.now().toString());
		subtractionResponseDetails.setApino(subtractionResponseCommon.getSubtractionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId, String symbol) {
		log.info(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog3);
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId) ||
			stringUtil.isNullorEmptytoString(symbol)) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
	
	private Response F_ResultInsert(String userId, Integer number1, Integer number2, String symbol, Integer result) {
		log.info(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog5);
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12))){
			connection.setAutoCommit(false);
			try(PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.InsertSQL)){
				preparedStatement.setString(1, userId);
				preparedStatement.setInt(2, number1);
				preparedStatement.setInt(3, number2);
				preparedStatement.setString(4, symbol);
				preparedStatement.setInt(5, result);
				preparedStatement.executeUpdate();
				connection.commit();
			}
		} catch (ClassNotFoundException classNotFoundException) {
			log.fatal(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog6);
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
		} catch (SQLException sqlException) {
			log.error(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog7);
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
		}
		
		log.info(GlobalSubtractionJDBCInsertLogVariable.SubtractionJDBCInsertLog8);
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
}
