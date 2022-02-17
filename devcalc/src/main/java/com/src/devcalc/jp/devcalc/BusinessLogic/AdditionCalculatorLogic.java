package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalAdditionLogVariable;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.AdditionResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;
import com.src.devcalc.jp.devcalc.Util.StringUtil;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.AdditionCalculatorJDBCInsertLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.AdditionCalculatorJDBCSelectLogic;

@RequestScoped
public class AdditionCalculatorLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//AdditionCalculatorJDBCLogicクラスのインスタンス化
	AdditionCalculatorJDBCSelectLogic additionCalculatorJDBCSelectLogic = new AdditionCalculatorJDBCSelectLogic();
	
	//AdditionCalculatorJDBCInsertLogicのインスタンス化
	AdditionCalculatorJDBCInsertLogic additionCalculatorJDBCInsertLogic = new AdditionCalculatorJDBCInsertLogic();
	
	private static Logger log;
	
	//デフォルトコンストラクタ
	public AdditionCalculatorLogic() {
		log = LogManager.getLogger(AdditionCalculatorLogic.this);
	}
	
	public Response F_AdditionResponse(AdditionResponseCommon additionResponseCommon) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		additionResponseDetails.setStatus(additionResponseCommon.getAdditionResponseStatus());
		additionResponseDetails.setMessage(additionResponseCommon.getAdditionResponseMessage());
		additionResponseDetails.setTime(LocalDateTime.now().toString());
		additionResponseDetails.setApino(additionResponseCommon.getAdditionResponseAPINo());
		return Response.status(additionResponseCommon.getStatus()).entity(additionResponseDetails).build();
	}
	
	public Response F_AdditionService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		AdditionResponseCommon additionResponseCommon = AdditionResponseCommon.AdditionS200;
		StringBuilder additionResult = new StringBuilder();
		
		Response additionResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
		if(additionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return additionResponse;
		}else {
			log.info(GlobalAdditionLogVariable.AdditionLog1);
		}
		
		additionResponse = F_CheckUserIDSelect(requestBodyEntity);
		if(additionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return additionResponse;
		}else {
			log.info(GlobalAdditionLogVariable.AdditionLog2);
		}
		
		additionResponse = F_AdditionCalculate(requestBodyEntity, additionResult);
		
		additionResponse = F_AdditionCalculatorInsert(requestBodyEntity, additionResult);
		if(additionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return additionResponse;
		}else {
			log.info(GlobalAdditionLogVariable.AdditionLog3);
		}
		
		additionResponseDetails.setStatus(additionResponseCommon.getAdditionResponseStatus());
		additionResponseDetails.setMessage(additionResponseCommon.getAdditionResponseMessage());
		additionResponseDetails.setTime(LocalDateTime.now().toString());
		additionResponseDetails.setResult(additionResult.toString());
		additionResponseDetails.setApino(additionResponseCommon.getAdditionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}

	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		log.info(GlobalAdditionLogVariable.AdditionLog4);
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid())) {
			return F_AdditionResponse(AdditionResponseCommon.AdditionE400);
		}else {
			//Not Execute
		}
		
		log.info(GlobalAdditionLogVariable.AdditionLog5);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
	
	private Response F_CheckUserIDSelect(RequestBodyEntity requestBodyEntity) {
		log.info(GlobalAdditionLogVariable.AdditionLog6);
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		String userId = new String();
		userId = requestBodyEntity.getuserid();
		Response AdditionCalculatorJDBCSelectLogicResponse = additionCalculatorJDBCSelectLogic.F_AdditionJDBCService(userId);
		
		if(AdditionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalAdditionLogVariable.AdditionLog7);
			return F_AdditionResponse(AdditionResponseCommon.AdditionE400);
		}else if(AdditionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()){
			log.error(GlobalAdditionLogVariable.AdditionLog8);
			return F_AdditionResponse(AdditionResponseCommon.AdditionE404);
		}else if(AdditionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalAdditionLogVariable.AdditionLog9);
			return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
		}else {
			log.info(GlobalAdditionLogVariable.AdditionLog10);
		}
		
		log.info(GlobalAdditionLogVariable.AdditionLog11);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
	
	private Response F_AdditionCalculate(RequestBodyEntity requestBodyEntity, StringBuilder additionResult) {
		log.info(GlobalAdditionLogVariable.AdditionLog12);
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		int additionNum1 = 0;
		int additionNum2 = 0;
		int Result = 0;
		
		additionNum1 = requestBodyEntity.getnum1(); 
		additionNum2 = requestBodyEntity.getnum2();
		
		Result = additionNum1 + additionNum2;
		additionResult.append(String.valueOf(Result));
		
		log.info(GlobalAdditionLogVariable.AdditionLog13);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
	
	private Response F_AdditionCalculatorInsert(RequestBodyEntity requestBodyEntity, StringBuilder additionResult) {
		log.info(GlobalAdditionLogVariable.AdditionLog14);
		CalculatorResponseDetails additionResponseDetails = new CalculatorResponseDetails();
		
		String userId = requestBodyEntity.getuserid();
		Integer number1 = requestBodyEntity.getnum1();
		Integer number2 = requestBodyEntity.getnum2();
		String symbol = GlobalLogicVariable.additionSymbol;
		Integer result = Integer.parseInt(additionResult.toString());
		Response AdditionCalculatorJDBCInsertLogicResponse = additionCalculatorJDBCInsertLogic.F_AdditionJDBCService(userId, number1, number2, symbol, result);
		
		if(AdditionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			log.fatal(GlobalAdditionLogVariable.AdditionLog15);
			return F_AdditionResponse(AdditionResponseCommon.AdditionE400);
		}else if(AdditionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			log.error(GlobalAdditionLogVariable.AdditionLog16);
			return F_AdditionResponse(AdditionResponseCommon.AdditionE500);
		}else {
			log.info(GlobalAdditionLogVariable.AdditionLog17);
		}
		
		log.info(GlobalAdditionLogVariable.AdditionLog18);
		return Response.status(Response.Status.OK.getStatusCode()).entity(additionResponseDetails).build();
	}
}
