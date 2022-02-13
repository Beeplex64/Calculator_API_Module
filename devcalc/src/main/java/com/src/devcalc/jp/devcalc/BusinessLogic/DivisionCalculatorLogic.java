package com.src.devcalc.jp.devcalc.BusinessLogic;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;
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
		
		//DataSourceHolderクラスのインスタンス化
		DataSourceHolder dataSourceHolder = new DataSourceHolder();
		
		//DivisionCalculatorJDBCSelectLogicクラスのインスタンス化
		DivisionCalculatorJDBCSelectLogic divisionCalculatorJDBCSelectLogic = new DivisionCalculatorJDBCSelectLogic();
		
		//DivisionCalculatorJDBCInsertLogicクラスのインスタンス化
		DivisionCalculatorJDBCInsertLogic divisionCalculatorJDBCInsertLogic = new DivisionCalculatorJDBCInsertLogic();
		
		//デフォルトコンストラクタ
		public DivisionCalculatorLogic() {
			
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
			StringBuilder divisionSEQ = new StringBuilder();
			
			Response divisionResponse = F_CheckRequestBody(requestEntity, requestBodyEntity);
			if(divisionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionResponse;
			}else {
				//Not Execute
			}
			
			divisionResponse = F_DivisionCalculatorSelect(requestBodyEntity);
			if(divisionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionResponse;
			}else {
				//Not Execute
			}
			
			divisionResponse = F_DivisionCalculate(requestBodyEntity, divisionResult);
			
			divisionResponse = F_DivisionCalculatorInsert(requestBodyEntity, divisionResult);
			if(divisionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
				return divisionResponse;
			}else {
				//Not Execute
			}
			
			divisionResponseDetails.setStatus(divisionResponseCommon.getDivisionResponseStatus());
			divisionResponseDetails.setMessage(divisionResponseCommon.getDivisionResponseMessage());
			divisionResponseDetails.setTime(LocalDateTime.now().toString());
			divisionResponseDetails.setResult(divisionResult.toString());
			divisionResponseDetails.setApino(divisionResponseCommon.getDivisionResponseAPINo());
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}

		private Response F_CheckRequestBody(RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			if(stringUtil.isNullorEmptytoString(requestBodyEntity.getuserid())) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else {
				//Not Execute
			}
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_DivisionCalculatorSelect(RequestBodyEntity requestBodyEntity) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			String userId = new String();
			userId = requestBodyEntity.getuserid();
			divisionCalculatorJDBCSelectLogic.F_DivisionService(userId);
			Response DivisionCalculatorJDBCSelectLogicResponse = divisionCalculatorJDBCSelectLogic.F_DivisionService(userId);
			
			if(DivisionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else if(DivisionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE404);
			}else if(DivisionCalculatorJDBCSelectLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
			}else {
				//Not Execute
			}
			
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_DivisionCalculate(RequestBodyEntity requestBodyEntity, StringBuilder divisionResult) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			int divisionNum1 = 0;
			int divisionNum2 = 0;
			int Result = 0;
			
			divisionNum1 = requestBodyEntity.getnum1();
			divisionNum2 = requestBodyEntity.getnum2();
			
			Result = divisionNum1 / divisionNum2;
			divisionResult.append(String.valueOf(Result));
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
		
		private Response F_DivisionCalculatorInsert(RequestBodyEntity requestBodyEntity, StringBuilder divisionResult) {
			CalculatorResponseDetails divisionResponseDetails = new CalculatorResponseDetails();
			
			String userId = requestBodyEntity.getuserid();
			Integer number1 = requestBodyEntity.getnum1();
			Integer number2 = requestBodyEntity.getnum2();
			String symbol = GlobalLogicVariable.divisionSymbol;
			Integer result = Integer.parseInt(divisionResult.toString());
			
			divisionCalculatorJDBCInsertLogic.F_DivisionJDBCService(userId, number1, number2, symbol, result);
			Response DivisionCalculatorJDBCInsertLogicResponse = divisionCalculatorJDBCInsertLogic.F_DivisionJDBCService(userId, number1, number2, symbol, result);
			if(DivisionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE400);
			}else if(DivisionCalculatorJDBCInsertLogicResponse.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				return F_DivisionResponse(DivisionResponseCommon.DivisionE500);
			}else {
				//Not Execute
			}
			
			return Response.status(Response.Status.OK.getStatusCode()).entity(divisionResponseDetails).build();
		}
}
