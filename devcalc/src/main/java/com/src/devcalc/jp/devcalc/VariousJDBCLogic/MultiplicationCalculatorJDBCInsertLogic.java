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
import com.src.devcalc.jp.devcalc.ResponseContent.MultiplicationResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class MultiplicationCalculatorJDBCInsertLogic {

		//StringUtilクラスのインスタンス化
		StringUtil stringUtil = new StringUtil();
		
		//DataSourceHolderクラスのインスタンス化
		DataSourceHolder dataSourceHolder = new DataSourceHolder();
		
		//デフォルトコンストラクタ
		public MultiplicationCalculatorJDBCInsertLogic() {
			
		}
		
		public Response F_MultiplicationResponse(MultiplicationResponseCommon multiplicationResponseCommon) {
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			multiplicationResponseDetails.setStatus(multiplicationResponseCommon.getMultiplicationResponseStatus());
			multiplicationResponseDetails.setMessage(multiplicationResponseCommon.getMultiplicationResponseMessage());
			multiplicationResponseDetails.setTime(LocalDateTime.now().toString());
			multiplicationResponseDetails.setApino(multiplicationResponseCommon.getMultiplicationResponseAPINo());
			return Response.status(multiplicationResponseCommon.getStatus()).entity(multiplicationResponseDetails).build();
		}
		
		public Response F_MultiplicationJDBCService(String userId, Integer number1, Integer number2, String symbol, Integer result) {
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			MultiplicationResponseCommon multiplicationResponseCommon = MultiplicationResponseCommon.MultiplicationS200;
			
			Response multiplicationJDBCResponse = F_CheckRequestBody(userId, symbol);
			if(multiplicationJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return multiplicationJDBCResponse;
			}else {
				//Not Execute
			}
			
			multiplicationJDBCResponse = F_ResultInsert(userId, number1, number2, symbol, result);
			if(multiplicationJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return multiplicationJDBCResponse;
			}else {
				//Not Execute
			}
			
			multiplicationResponseDetails.setStatus(multiplicationResponseCommon.getMultiplicationResponseStatus());
			multiplicationResponseDetails.setMessage(multiplicationResponseCommon.getMultiplicationResponseMessage());
			multiplicationResponseDetails.setTime(LocalDateTime.now().toString());
			multiplicationResponseDetails.setApino(multiplicationResponseCommon.getMultiplicationResponseAPINo());
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}
		
		private Response F_CheckRequestBody(String userId, String symbol) {
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			if(stringUtil.isNullorEmptytoString(userId) ||
				stringUtil.isNullorEmptytoString(symbol)){
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE400);
			}else {
				//Not Execute
			}
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}
		
		private Response F_ResultInsert(String userId, Integer number1, Integer number2, String symbol, Integer result) {
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
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
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE500);
			} catch (SQLException sqlException) {
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE500);
			}
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}
}
