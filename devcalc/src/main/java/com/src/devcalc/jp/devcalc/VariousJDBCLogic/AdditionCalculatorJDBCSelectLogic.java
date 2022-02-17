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

import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalAdditionJDBCSelectLogVariable;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.AdditionResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.BusinessLogic.UserLoginLogic;
import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class AdditionCalculatorJDBCSelectLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public AdditionCalculatorJDBCSelectLogic () {
		log = LogManager.getLogger(AdditionCalculatorJDBCSelectLogic.this);
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
		if(additionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return additionJDBCResponse;
		}else {
			log.info(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog1);
		}
		
		additionJDBCResponse = F_CheckUserIDSelect(userId);
		if(additionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return additionJDBCResponse;
		}else {
			log.info(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog2);
		}
		
		additionResponseDetails.setStatus(additionResponseCommon.getAdditionResponseStatus());
		additionResponseDetails.setMessage(additionResponseCommon.getAdditionResponseMessage());
		additionResponseDetails.setTime(LocalDateTime.now().toString());
		additionResponseDetails.setApino(additionResponseCommon.getAdditionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}

	private Response F_CheckRequestBody(String userId) {
		log.info(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog3);
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_AdditionResponse(AdditionResponseCommon.AdditionE400);
		}else {
			//Not Execute
		}
		log.info(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog4);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
	
	private Response F_CheckUserIDSelect(String userId) {
		log.info(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog5);
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
						log.error(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog6);
						return F_AdditionResponse(AdditionResponseCommon.AdditionE404);
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.fatal(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog7 + retryMaxCount);
					return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					log.error(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog8 + retryMaxCount);
					return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
				}
			}
			break;
		}
		
		log.info(GlobalAdditionJDBCSelectLogVariable.AdditionJDBCSelectLog9);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
}
