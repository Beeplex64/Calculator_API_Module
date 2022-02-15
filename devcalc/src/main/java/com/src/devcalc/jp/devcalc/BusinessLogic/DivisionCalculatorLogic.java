package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalDivisionLogVariable;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.DivisionResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.DivisionCalculatorJDBCInsertLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.DivisionCalculatorJDBCSelectLogic;

@RequestScoped
public class DivisionCalculatorLogic {
	
		//StringUtilクラスのインスタンス化
		StringUtil stringUtil = new StringUtil();
		
		//RequestEntityクラスのインスタンス化
		RequestEntity requestEntity = new RequestEntity();
		
		//RequestBodyEntityクラスのインスタンス化
		RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
		
		//DivisionCalculatorJDBCSelectLogicクラスのインスタンス化
		DivisionCalculatorJDBCSelectLogic divisionCalculatorJDBCSelectLogic = new DivisionCalculatorJDBCSelectLogic();
		
		//DivisionCalculatorJDBCInsertLogicクラスのインスタンス化
		DivisionCalculatorJDBCInsertLogic divisionCalculatorJDBCInsertLogic = new DivisionCalculatorJDBCInsertLogic();
		
		private static Logger log;
		
		//デフォルトコンストラクタ
		public DivisionCalculatorLogic() {
			log = LogManager.getLogger(DivisionCalculatorLogic.this);
		}
		
		public Response F_DivisionResponse(DivisionResponseCommon divisionResponseCommon) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(divisionResponseCommon.getStatus()).entity(divisionResponseDetails).build();
		}
		
		public Response F_DivisionService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			DivisionResponseCommon divisionResponseCommon = DivisionResponseCommon.DivisionS200;
			StringBuilder divisionResult = new StringBuilder();
			
			Response divisionResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
			if(divisionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionResponse;
			}else {
				log.info(GlobalDivisionLogVariable.DivisionLog1);
			}
			
			divisionResponse = F_DivisionCalculatorSelect(requestBodyEntity);
			if(divisionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionResponse;
			}else {
				log.info(GlobalDivisionLogVariable.DivisionLog2);
			}
			
			divisionResponse = F_DivisionCalculate(requestBodyEntity, divisionResult);
			
			divisionResponse = F_DivisionCalculatorInsert(requestBodyEntity, divisionResult);
			if(divisionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionResponse;
			}else {
				log.info(GlobalDivisionLogVariable.DivisionLog3);
			}
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setResult(divisionResult.toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}

		private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
			log.info(GlobalDivisionLogVariable.DivisionLog4);
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid())) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else {
				//Not Execute
			}
			
			log.info(GlobalDivisionLogVariable.DivisionLog5);
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_DivisionCalculatorSelect(RequestBodyEntity requestBodyEntity) {
			log.info(GlobalDivisionLogVariable.DivisionLog6);
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			String userId = new String();
			userId = requestBodyEntity.getuserid();
			Response DivisionCalculatorJDBCSelectLogicResponse = divisionCalculatorJDBCSelectLogic.F_DivisionService(userId);
			
			if(DivisionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
				log.fatal(GlobalDivisionLogVariable.DivisionLog7);
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else if(DivisionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
				log.error(GlobalDivisionLogVariable.DivisionLog8);
				return F_DivisionResponse(DivisionResponseCommon.DivisionE404);
			}else if(DivisionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				log.error(GlobalDivisionLogVariable.DivisionLog9);
				return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
			}else {
				log.info(GlobalDivisionLogVariable.DivisionLog10);
			}
			
			log.info(GlobalDivisionLogVariable.DivisionLog11);
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_DivisionCalculate(RequestBodyEntity requestBodyEntity, StringBuilder divisionResult) {
			log.info(GlobalDivisionLogVariable.DivisionLog12);
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			int divisionNum1 = 0;
			int divisionNum2 = 0;
			int Result = 0;
			
			divisionNum1 = requestBodyEntity.getnum1();
			divisionNum2 = requestBodyEntity.getnum2();
			
			Result = divisionNum1 / divisionNum2;
			divisionResult.append(String.valueOf(Result));
			
			log.info(GlobalDivisionLogVariable.DivisionLog13);
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_DivisionCalculatorInsert(RequestBodyEntity requestBodyEntity, StringBuilder divisionResult) {
			log.info(GlobalDivisionLogVariable.DivisionLog14);
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			String userId = requestBodyEntity.getuserid();
			Integer number1 = requestBodyEntity.getnum1();
			Integer number2 = requestBodyEntity.getnum2();
			String symbol = GlobalLogicVariable.divisionSymbol;
			Integer result = Integer.parseInt(divisionResult.toString());
			Response DivisionCalculatorJDBCInsertLogicResponse = divisionCalculatorJDBCInsertLogic.F_DivisionJDBCService(userId, number1, number2, symbol, result);
			
			if(DivisionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
				log.fatal(GlobalDivisionLogVariable.DivisionLog15);
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else if(DivisionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				log.error(GlobalDivisionLogVariable.DivisionLog16);
				return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
			}else {
				log.info(GlobalDivisionLogVariable.DivisionLog17);
			}
			
			log.info(GlobalDivisionLogVariable.DivisionLog18);
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
}
