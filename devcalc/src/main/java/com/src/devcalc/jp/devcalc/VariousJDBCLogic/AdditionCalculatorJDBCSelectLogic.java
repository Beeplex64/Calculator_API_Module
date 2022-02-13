package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.AdditionResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class AdditionCalculatorJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	//デフォルトコンストラクタ
	public AdditionCalculatorJDBCSelectLogic () {
		
	}
	
	public Response F_AdditionResponse(AdditionResponseCommon additionResponseCommon) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		additionResponseDetails.setStatus(additionResponseCommon.getAdditionResponseStatus());
		additionResponseDetails.setMessage(additionResponseCommon.getAdditionResponseMessage());
		additionResponseDetails.setTime(LocalDateTime.now().toString());
		additionResponseDetails.setApino(additionResponseCommon.getAdditionResponseAPINo());
		return Response.status(additionResponseCommon.getStatus()).entity(additionResponseDetails).build();
	}
	
	public Response F_AdditionJDBCService(String userId) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		AdditionResponseCommon additionResponseCommon = AdditionResponseCommon.AdditionS200;
		
		Response additionJDBCResponse = F_CheckRequestBody(userId);
		if(additionJDBCResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			return F_AdditionResponse(AdditionResponseCommon.AdditionE400);
		}else {
			//Not Execute
		}
		
		additionJDBCResponse = F_CheckUserIDSelect(userId);
		if(additionJDBCResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return F_AdditionResponse(AdditionResponseCommon.AdditionE404);
		}else if(additionJDBCResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
			return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
		}else {
			//Not Execute
		}
		
		additionResponseDetails.setStatus(additionResponseCommon.getAdditionResponseStatus());
		additionResponseDetails.setMessage(additionResponseCommon.getAdditionResponseMessage());
		additionResponseDetails.setTime(LocalDateTime.now().toString());
		additionResponseDetails.setApino(additionResponseCommon.getAdditionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}

	private Response F_CheckRequestBody(String userId) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_AdditionResponse(AdditionResponseCommon.AdditionE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
	
	private Response F_CheckUserIDSelect(String userId) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement.setString(1, userId);
				
				try(ResultSet resultSet = preparedStatement.executeQuery()){
					if(resultSet.next()) {
						//Not Execute
					}else {
							return F_AdditionResponse(AdditionResponseCommon.AdditionE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
				}
			}
			break;
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
}
