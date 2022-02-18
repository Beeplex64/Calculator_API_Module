package com.src.devcalc.jp.devcalc.Resource;

import java.util.concurrent.Future;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.src.devcalc.jp.devcalc.BusinessLogic.AdditionCalculatorLogic;
import com.src.devcalc.jp.devcalc.BusinessLogic.DivisionCalculatorLogic;
import com.src.devcalc.jp.devcalc.BusinessLogic.MultiplicationCalculatorLogic;
import com.src.devcalc.jp.devcalc.BusinessLogic.SubtractionCalculatorLogic;
import com.src.devcalc.jp.devcalc.BusinessLogic.UserLogicalDeleteLogic;
import com.src.devcalc.jp.devcalc.BusinessLogic.UserLoginLogic;
import com.src.devcalc.jp.devcalc.BusinessLogic.UserRegistretionLogic;
import com.src.devcalc.jp.devcalc.BusinessLogic.UserRequestLogic;
import com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic.AsyncUserInquiryLogicCall;
import com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic.AsyncUserLogicalDeleteLogicCall;
import com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic.AsyncUserRegistrationLogicCall;
import com.src.devcalc.jp.devcalc.CallAsyncBusinessLogic.AsyncUserRequestLogicCall;
import com.src.devcalc.jp.devcalc.Entity.RequestBodyEntity;
import com.src.devcalc.jp.devcalc.Entity.RequestEntity;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("")
public class DevCalcResources {
	
	@Inject
	//AdditionCalculationLogicのインジェクション
	AdditionCalculatorLogic additionCalculatorLogic = new AdditionCalculatorLogic();
	
	//SubtractionCalculatorLogicのインジェクション
	SubtractionCalculatorLogic subtractionCalculatorLogic = new SubtractionCalculatorLogic();
	
	//MultiplicationCalculatorLogicのインジェクション
	MultiplicationCalculatorLogic multiplicationCalculatorLogic = new MultiplicationCalculatorLogic();

	//DivisionCalculatorLogicのインジェクション
	DivisionCalculatorLogic divisionCalculatorLogic = new DivisionCalculatorLogic();
	
	//UserRegistretionLogicクラスのインジェクション
	AsyncUserRegistrationLogicCall asyncUserRegistrationLogicCall = new AsyncUserRegistrationLogicCall();
	
	//UserLoginLogicクラスのインジェクション
	UserLoginLogic userLoginLogic = new UserLoginLogic();
	
	//AsyncUserLogicalDeleteLogicCallクラスのインジェクション
	AsyncUserLogicalDeleteLogicCall asyncUserLogicalDeleteLogicCall = new AsyncUserLogicalDeleteLogicCall();
	
	//AsyncUserRequestLogicCallクラスのインジェクション
	AsyncUserRequestLogicCall asyncUserRequestLogicCall = new AsyncUserRequestLogicCall();
	
	// AsyncUserInquiryLogicCallクラスのインジェクション
	 AsyncUserInquiryLogicCall  asyncUserInquiryLogicCall = new  AsyncUserInquiryLogicCall();
	
	@GET
	@Path("/addition")
	public Response AdditionCalcLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return additionCalculatorLogic.F_AdditionService(requestEntity, requestBodyEntity);
	}
	
	@GET
	@Path("/subtraction")
	public Response SubtractionCalcLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return subtractionCalculatorLogic.F_SubtractionService(requestEntity, requestBodyEntity);
	}
	
	@GET
	@Path("/multiplication")
	public Response MultiplicationCalcLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return multiplicationCalculatorLogic.F_MultiplicationService(requestEntity, requestBodyEntity);
	}
	
	@GET
	@Path("/division")
	public Response DivisionCalcLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return divisionCalculatorLogic.F_DivisionService(requestEntity, requestBodyEntity);
	}
	
	@GET
	@Path("/login")
	public Response LoginLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return userLoginLogic.F_UserLoginService(requestEntity, requestBodyEntity);
	}
	
	@POST
	@Path("/registuser")
	public Response RegistLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return asyncUserRegistrationLogicCall.CallAsyncUserRegistrationLogicService(requestEntity, requestBodyEntity);
	}
	
	@POST
	@Path("/request")
	public Response RequestLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return asyncUserRequestLogicCall.CallAsyncUserRequestLogicService(requestEntity, requestBodyEntity);
	}
	
	@POST
	@Path("/inquiry")
	public Response InquiryLogic(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return asyncUserInquiryLogicCall.CallAsyncUserInquiryLogicService(requestEntity, requestBodyEntity);
	}
	
	@DELETE
	@Path("/withdrawal")
	public Response UserDelete(@BeanParam RequestEntity requestEntity, RequestBodyEntity requestBodyEntity) {
		return asyncUserLogicalDeleteLogicCall.CallAsyncUserLogicalDeleteLogicService(requestEntity, requestBodyEntity);
	}
}
