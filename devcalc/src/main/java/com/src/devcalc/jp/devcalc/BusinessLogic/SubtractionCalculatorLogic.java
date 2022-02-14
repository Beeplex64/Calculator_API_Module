package com.src.devcalc.jp.devcalc.BusinessLogic;

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
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.SubtractionCalculatorJDBCInsertLogic;
import com.src.devcalc.jp.devcalc.VariousJDBCLogic.SubtractionCalculatorJDBCSelectLogic;

@RequestScoped
public class SubtractionCalculatorLogic {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//RequestEntityクラスのインスタンス化
	RequestEntity requestEntity = new RequestEntity();
	
	//RequestBodyEntityクラスのインスタンス化
	RequestBodyEntity requestBodyEntity = new RequestBodyEntity();
	
	//SubtractionCalculatorJDBCSelectLogicクラスのインスタンス化
	SubtractionCalculatorJDBCSelectLogic subtractionCalculatorJDBCSelectLogic = new SubtractionCalculatorJDBCSelectLogic();
	
	//SubtractionCalculatorJDBCInsertLogicクラスのインスタンス化
	SubtractionCalculatorJDBCInsertLogic subtractionCalculatorJDBCInsertLogic = new SubtractionCalculatorJDBCInsertLogic();
	
	//デフォルトコンストラクタ
	public SubtractionCalculatorLogic() {
		
	}
	
	public Response F_SubtractionResponse(SubtractionResponseCommon subtractionResponseCommon) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		subtractionResponseDetails.setStatus(subtractionResponseCommon.getSubtractionResponseStatus());
		subtractionResponseDetails.setMessage(subtractionResponseCommon.getSubtractionResponseMessage());
		subtractionResponseDetails.setTime(LocalDateTime.now().toString());
		subtractionResponseDetails.setApino(subtractionResponseCommon.getSubtractionResponseAPINo());
		return Response.status(subtractionResponseCommon.getStatus()).entity(subtractionResponseDetails).build();
	}
	
	public Response F_SubtractionService(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		SubtractionResponseCommon subtractionResponseCommon = SubtractionResponseCommon.SubtractionS200;
		StringBuilder subtractionResult = new StringBuilder();
		
		Response subtractionResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
		if(subtractionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionResponse;
		}else {
			//Not Execute
		}
		
		subtractionResponse = F_SubtractionCalculatorSelect(requestBodyEntity);
		if(subtractionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionResponse;
		}else {
			//Not Execute
		}
		
		subtractionResponse = F_SubtractionCalculate(requestBodyEntity, subtractionResult);
		
		subtractionResponse = F_SubtractionInsert(requestBodyEntity, subtractionResult);
		if(subtractionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return subtractionResponse;
		}else {
			//Not Execute
		}
		
		subtractionResponseDetails.setStatus(subtractionResponseCommon.getSubtractionResponseStatus());
		subtractionResponseDetails.setMessage(subtractionResponseCommon.getSubtractionResponseMessage());
		subtractionResponseDetails.setTime(LocalDateTime.now().toString());
		subtractionResponseDetails.setResult(subtractionResult.toString());
		subtractionResponseDetails.setApino(subtractionResponseCommon.getSubtractionResponseAPINo());
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
	private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid())) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}

	private Response F_SubtractionCalculatorSelect(RequestBodyEntity requestBodyEntity) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		String userId = new String();
		userId = requestBodyEntity.getuserid();
		Response SubtractionCalculatorJDBCSelectLogic = subtractionCalculatorJDBCSelectLogic.F_SubtractionService(userId);
		
		if(SubtractionCalculatorJDBCSelectLogic.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE400);
		}else if(SubtractionCalculatorJDBCSelectLogic.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE404);
		}else if(SubtractionCalculatorJDBCSelectLogic.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
		}
		
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
	
	private Response F_SubtractionCalculate(RequestBodyEntity requestBodyEntity, StringBuilder subtractionResult) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		int subtractionNum1 = 0;
		int subtractionNum2 = 0;
		int Result = 0;
		
		subtractionNum1 = requestBodyEntity.getnum1(); 
		subtractionNum2 = requestBodyEntity.getnum2();
		
		Result = subtractionNum1 - subtractionNum2;
		subtractionResult.append(String.valueOf(Result));
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
	
	private Response F_SubtractionInsert(RequestBodyEntity requestBodyEntity, StringBuilder subtractionResult) {
		CalculatorResponseDetails subtractionResponseDetails = new CalculatorResponseDetails();
		
		String userId = requestBodyEntity.getuserid();
		Integer number1 = requestBodyEntity.getnum1();
		Integer number2 = requestBodyEntity.getnum2();
		String symbol = GlobalLogicVariable.subtractionSymbol;
		Integer result = Integer.parseInt(subtractionResult.toString());
		Response SubtractionCalculatorJDBCInsertLogicResponse = subtractionCalculatorJDBCInsertLogic.F_SubtractionJDBCService(userId, number1, number2, symbol, result);
		
		if(SubtractionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE400);
		}else if(SubtractionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			return F_SubtractionResponse(SubtractionResponseCommon.SubtractionE500);
		}
		
		return Response.status(Response.Status.OK.getStatusCode()).entity(subtractionResponseDetails).build();
	}
}
