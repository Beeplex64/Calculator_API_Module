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
import com.src.devcalc.jp.devcalc.ResponseContent.DivisionResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.MultiplicationResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class DivisionCalculatorJDBCSelectLogic {

		//StringUtilクラスのインスタンス化
		StringUtil stringUtil = new StringUtil();
		
		//DataSourceHolderクラスのインスタンス化
		DataSourceHolder dataSourceHolder = new DataSourceHolder();
		
		//デフォルトコンストラクタ
		public DivisionCalculatorJDBCSelectLogic() {
			
		}
		
		public Response F_DivisionResponse(DivisionResponseCommon divisionResponseCommon) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(divisionResponseCommon.getStatus()).entity(divisionResponseDetails).build();
		}
		
		public Response F_DivisionService(String userId) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			DivisionResponseCommon divisionResponseCommon = DivisionResponseCommon.DivisionS200;
			
			Response divisionJDBCResponse = F_CheckRequestBody(userId);
			if(divisionJDBCResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else {
				//Not Execute
			}
			
			divisionJDBCResponse = F_CheckUserIDSelect(userId);
			if(divisionJDBCResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE404);
			}else if(divisionJDBCResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()){
				return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
			}
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_CheckRequestBody(String userId) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			if(stringUtil.isNullorEmptytoString(userId)) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else {
				//Not Execute
			}
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_CheckUserIDSelect(String userId) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			int retryMaxCount = 0;
			
			while(retryMaxCount < 3) {
				try(Connection connection = dataSourceHolder.connection(Duration.ofSeconds(12));
							PreparedStatement preparedStatement = connection.prepareStatement(GlobalLogicVariable.SelectSQL)){
					preparedStatement.setString(1, userId);
					
					try(ResultSet resultSet = preparedStatement.executeQuery()){
						if(resultSet.next()) {
							//Not Execute
						}else {
							return F_DivisionResponse(DivisionResponseCommon.DivisionE404);
						}
					}
				} catch (ClassNotFoundException classNotFoundException) {
					retryMaxCount ++;
					if(retryMaxCount < 3) {
						continue;
					}else {
						return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
					}
				} catch (SQLException sqlException) {
					retryMaxCount ++;
					if(retryMaxCount < 3) {
						continue;
					}else {
						return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
					}
				}
				break;
			}
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
}
