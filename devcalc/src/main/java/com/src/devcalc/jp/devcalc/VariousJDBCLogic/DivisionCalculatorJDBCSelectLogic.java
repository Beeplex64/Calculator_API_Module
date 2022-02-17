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
import com.src.devcalc.jp.devcalc.GlobalVariable.GrobalDivisionJDBCSelectLogVariable;
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
		
		private static Logger log;
		
		//デフォルトコンストラクタ
		public DivisionCalculatorJDBCSelectLogic() {
			log = LogManager.getLogger(DivisionCalculatorJDBCSelectLogic.this);
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
			if(divisionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionJDBCResponse;
			}else {
				log.info(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog1);
			}
			
			divisionJDBCResponse = F_CheckUserIDSelect(userId);
			if(divisionJDBCResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionJDBCResponse;
			}else {
				log.info(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog2);
			}
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_CheckRequestBody(String userId) {
			log.info(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog3);
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			if(stringUtil.isNullorEmptytoString(userId)) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else {
				//Not Execute
			}
			
			log.info(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog4);
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_CheckUserIDSelect(String userId) {
			log.info(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog5);
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
							log.error(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog6);
							return F_DivisionResponse(DivisionResponseCommon.DivisionE404);
						}
					}
				} catch (ClassNotFoundException classNotFoundException) {
					retryMaxCount ++;
					if(retryMaxCount < 3) {
						continue;
					}else {
						log.fatal(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog7 + classNotFoundException);
						return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
					}
				} catch (SQLException sqlException) {
					retryMaxCount ++;
					if(retryMaxCount < 3) {
						continue;
					}else {
						log.error(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog8 + sqlException);
						return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
					}
				}
				break;
			}
			
			log.info(GrobalDivisionJDBCSelectLogVariable.DivisionJDBCSelectLog9);
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
}
