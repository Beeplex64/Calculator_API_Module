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

import com.src.devcalc.jp.devcalc.BusinessLogic.DivisionCalculatorLogic;
import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalAdditionJDBCInsertLogVariable;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.AdditionResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class AdditionCalculatorJDBCInsertLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public AdditionCalculatorJDBCInsertLogic() {
		log = LogManager.getLogger(AdditionCalculatorJDBCInsertLogic.this);
	}
	
	public Response F_AdditionResponse(AdditionResponseCommon additionResponseCommon) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		additionResponseDetails.setStatus(additionResponseCommon.getAdditionResponseStatus());
		additionResponseDetails.setMessage(additionResponseCommon.getAdditionResponseMessage());
		additionResponseDetails.setTime(LocalDateTime.now().toString());
		additionResponseDetails.setApino(additionResponseCommon.getAdditionResponseAPINo());
		return Response.status(additionResponseCommon.getStatus()).entity(additionResponseDetails).build();
	}
	
	public Response F_AdditionJDBCService(String userId, Integer number1, Integer number2, String symbol, Integer result) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		AdditionResponseCommon additionResponseCommon = AdditionResponseCommon.AdditionS200;
		
		Response additionJDBCResponse = F_CheckRequestBody(userId, symbol);
		if(additionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return additionJDBCResponse;
		}else {
			log.info(GlobalAdditionJDBCInsertLogVariable.AddtionJDBCInsertLog1);
		}
		
		additionJDBCResponse = F_ResultInsert(userId, number1, number2, symbol, result);
		if(additionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return additionJDBCResponse;
		}else {
			log.info(GlobalAdditionJDBCInsertLogVariable.AdditionJDBCInsertLog2);
		}
		
		additionResponseDetails.setStatus(additionResponseCommon.getAdditionResponseStatus());
		additionResponseDetails.setMessage(additionResponseCommon.getAdditionResponseMessage());
		additionResponseDetails.setTime(LocalDateTime.now().toString());
		additionResponseDetails.setApino(additionResponseCommon.getAdditionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}

	private Response F_CheckRequestBody(String userId, String symbol) {
		log.info(GlobalAdditionJDBCInsertLogVariable.AdditionJDBCInsertLog3);
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId) ||
			stringUtil.isNullorEmptytoString(symbol)){
			return F_AdditionResponse(AdditionResponseCommon.AdditionE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalAdditionJDBCInsertLogVariable.AdditionJDBCInsertLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
	
	private Response F_ResultInsert(String userId, Integer number1, Integer number2, String symbol, Integer result) {
		log.info(GlobalAdditionJDBCInsertLogVariable.AdditionJDBCInsertLog5);
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
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
			log.fatal(GlobalAdditionJDBCInsertLogVariable.AdditionJDBCInsertLog6 + classNotFoundException);
			return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
		} catch (SQLException sqlException) {
			log.error(GlobalAdditionJDBCInsertLogVariable.AdditionJDBCInsertLog7 + sqlException);
			return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
		}
		
		log.info(GlobalAdditionJDBCInsertLogVariable.AdditionJDBCInsertLog8);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
}
