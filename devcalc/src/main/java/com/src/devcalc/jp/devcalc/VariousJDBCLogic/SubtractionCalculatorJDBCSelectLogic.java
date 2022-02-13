package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.src.devcalc.jp.devcalc.ResponseContent.SubtractionResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class SubtractionCalculatorJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	//デフォルトコンストラクタ
	public SubtractionCalculatorJDBCSelectLogic() {
		
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
			//Not Execute
		}
		
		subtractionJDBCResponse = F_CheckUserIDSelect(userId);
		if(subtractionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionJDBCResponse;
		}else {
			//Not Execute
		}
		
		subtractionResponseDetails.setStatus(subtractionResponseCommon.getSubtractionResponseStatus());
		subtractionResponseDetails.setMessage(subtractionResponseCommon.getSubtractionResponseMessage());
		subtractionResponseDetails.setTime(LocalDateTime.now().toString());
		subtractionResponseDetails.setApino(subtractionResponseCommon.getSubtractionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}

	private Response F_CheckUserIDSelect(String userId) {
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
						return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
				}
			}
			break;
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
}
