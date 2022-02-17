package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalMultiplicationLogVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.ResponseContent.MultiplicationResponseCommon;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.MultiplicationCalculatorJDBCInsertLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.MultiplicationCalculatorJDBCSelectLogic;

@RequestScoped
public class MultiplicationCalculatorLogic {
	
		//StringUtilクラスのインスタンス化
		StringUtil stringUtil = new StringUtil();
		
		//RequestEntityクラスのインスタンス化
		RequestEntity requestEntity = new RequestEntity();
		
		//RequestBodyEntityクラスのインスタンス化
		RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
		
		//MultiplicationCalculatorJDBCSelectLogicクラスのインスタンス化
		MultiplicationCalculatorJDBCSelectLogic multiplicationCalculatorJDBCLogic = new MultiplicationCalculatorJDBCSelectLogic();
		
		//MultiplicationCalculatorJDBCInsertLogicクラスのインスタンス化
		MultiplicationCalculatorJDBCInsertLogic multiplicationCalculatorJDBCInsertLogic = new MultiplicationCalculatorJDBCInsertLogic();
		
		private static Logger log;
		
		//デフォルトコンストラクタ
		public MultiplicationCalculatorLogic() {
			log = LogManager.getLogger(MultiplicationCalculatorLogic.this);
		}
		
		public Response F_MultiplicationResponse(MultiplicationResponseCommon multiplicationResponseCommon) {
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			multiplicationResponseDetails.setStatus(multiplicationResponseCommon.getMultiplicationResponseStatus());
			multiplicationResponseDetails.setMessage(multiplicationResponseCommon.getMultiplicationResponseMessage());
			multiplicationResponseDetails.setTime(LocalDateTime.now().toString());
			multiplicationResponseDetails.setApino(multiplicationResponseCommon.getMultiplicationResponseAPINo());
			return Response.status(multiplicationResponseCommon.getStatus()).entity(multiplicationResponseDetails).build();
		}
		
		public Response F_MultiplicationService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			MultiplicationResponseCommon multiplicationResponseCommon = MultiplicationResponseCommon.MultiplicationS200;
			StringBuilder multiplicationResult = new StringBuilder();
			
			Response multiplicationResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
			if(multiplicationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return multiplicationResponse;
			}else {
				log.info(GlobalMultiplicationLogVariable.MultiplicationLog1);
			}
			
			multiplicationResponse = F_MultiplicationCalculatorSelect(requestBodyEntity);
			if(multiplicationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return multiplicationResponse;
			}else {
				log.info(GlobalMultiplicationLogVariable.MultiplicationLog2);
			}
			
			multiplicationResponse = F_MultiplicationCalculate(requestBodyEntity, multiplicationResult);
			
			
			multiplicationResponse = F_MultiplicationInsert(requestBodyEntity, multiplicationResult);
			if(multiplicationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return multiplicationResponse;
			}else {
				log.info(GlobalMultiplicationLogVariable.MultiplicationLog3);
			}
			
			multiplicationResponseDetails.setStatus(multiplicationResponseCommon.getMultiplicationResponseStatus());
			multiplicationResponseDetails.setMessage(multiplicationResponseCommon.getMultiplicationResponseMessage());
			multiplicationResponseDetails.setTime(LocalDateTime.now().toString());
			multiplicationResponseDetails.setResult(multiplicationResult.toString());
			multiplicationResponseDetails.setApino(multiplicationResponseCommon.getMultiplicationResponseAPINo());
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}

		private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog4);
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid())) {
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE400);
			}else {
				//Not Execute
			}
			
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog5);
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}
		
		private Response F_MultiplicationCalculatorSelect(RequestBodyEntity requestBodyEntity) {
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog6);
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			String userId = new String();
			userId = requestBodyEntity.getuserid();
			Response MultiplicationCalculatorJDBCLogicResponse = multiplicationCalculatorJDBCLogic.F_MultiplicationService(userId);
			
			if(MultiplicationCalculatorJDBCLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
				log.fatal(GlobalMultiplicationLogVariable.MultiplicationLog7);
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE400);
			}else if(MultiplicationCalculatorJDBCLogicResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
				log.error(GlobalMultiplicationLogVariable.MultiplicationLog8);
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE404);
			}else if(MultiplicationCalculatorJDBCLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				log.error(GlobalMultiplicationLogVariable.MultiplicationLog9);
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE500);
			}else {
				log.info(GlobalMultiplicationLogVariable.MultiplicationLog10);
			}
			
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog11);
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}
		
		private Response F_MultiplicationCalculate(RequestBodyEntity requestBodyEntity, StringBuilder multiplicationResult) {
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog12);
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			int multiplicationNum1 = 0;
			int multiplicationNum2 = 0;
			int Result = 0;
			
			multiplicationNum1 = requestBodyEntity.getnum1(); 
			multiplicationNum2 = requestBodyEntity.getnum2();
			
			Result = multiplicationNum1 * multiplicationNum2;
			multiplicationResult.append(String.valueOf(Result));
			
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog13);
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}
		
		private Response F_MultiplicationInsert(RequestBodyEntity requestBodyEntity, StringBuilder multiplicationResult) {
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog14);
			CalculatorResponseDetails multiplicationResponseDetails = new CalculatorResponseDetails();
			
			String userId = requestBodyEntity.getuserid();
			Integer number1 = requestBodyEntity.getnum1();
			Integer number2 = requestBodyEntity.getnum2();
			String symbol = GlobalLogicVariable.multiplicationSymbol;
			Integer result = Integer.parseInt(multiplicationResult.toString());
			Response MultiplicationCalculatorJDBCInsertLogicResponse = multiplicationCalculatorJDBCInsertLogic.F_MultiplicationJDBCService(userId, number1, number2, symbol, result);
			
			if(MultiplicationCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
				log.fatal(GlobalMultiplicationLogVariable.MultiplicationLog15);
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE400);
			}else if(MultiplicationCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				log.error(GlobalMultiplicationLogVariable.MultiplicationLog16);
				return F_MultiplicationResponse(MultiplicationResponseCommon.MultiplicationE500);
			}else {
				log.error(GlobalMultiplicationLogVariable.MultiplicationLog17);
			}
			
			log.info(GlobalMultiplicationLogVariable.MultiplicationLog18);
			return Response.status(Response.Status.OK.getStatusCode()).entity(multiplicationResponseDetails).build();
		}
}
