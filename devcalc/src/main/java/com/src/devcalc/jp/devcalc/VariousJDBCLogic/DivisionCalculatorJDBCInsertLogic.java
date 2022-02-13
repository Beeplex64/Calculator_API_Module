package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.DivisionResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class DivisionCalculatorJDBCInsertLogic {
	
		//StringUtilクラスのインスタンス化
		StringUtil stringUtil = new StringUtil();
		
		//RequestEntityクラスのインスタンス化
		RequestEntity requestEntity = new RequestEntity();
		
		//RequestBodyEntityクラスのインスタンス化
		RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
		
		//DataSourceHolderクラスのインスタンス化
		DataSourceHolder dataSourceHolder = new DataSourceHolder();
		
		//デフォルトコンストラクタ
		public DivisionCalculatorJDBCInsertLogic() {
			
		}
		
		public Response F_DivisionResponse(DivisionResponseCommon divisionResponseCommon) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(divisionResponseCommon.getStatus()).entity(divisionResponseDetails).build();
		}
		
		public Response F_DivisionJDBCService(String userId, Integer number1, Integer number2, String symbol, Integer result) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			DivisionResponseCommon divisionResponseCommon = DivisionResponseCommon.DivisionS200;
			
			Response divisionJDBCResponse = F_CheckRequestBody(userId, symbol);
			if(divisionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionJDBCResponse;
			}else {
				//Not Execute
			}
			
			divisionJDBCResponse = F_ResultInsert(userId, number1, number2, symbol, result);
			if(divisionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionJDBCResponse;
			}else {
				//Not Execute
			}
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}

		private Response  F_CheckRequestBody(String userId, String symbol) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			if(stringUtil.isNullorEmptytoString(userId) ||
				stringUtil.isNullorEmptytoString(symbol)) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else {
				//Not Execute
			}
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_ResultInsert(String userId, Integer number1, Integer number2, String symbol, Integer result) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
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
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			} catch (SQLException sqlException) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
}
