package com.src.devcalc.jp.devcalc.GenerateVerifyJWT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;
import com.src.devcalc.jp.devcalc.ResponseContent.GenerateJWTResponseCommon;
import com.src.devcalc.jp.devcalc.ResponseContent.GenerateJWTResponseDetails;
import com.src.devcalc.jp.devcalc.Util.StringUtil;

@RequestScoped
public class GenerateAndVerifyJWTToken {
	
	//StringUtilクラスのインスタンス化
	StringUtil stringUtil = new StringUtil();
	
	//DataSourceHolderクラスのインスタンス化
	DataSourceHolder dataSourceHolder = new DataSourceHolder();
	
	//デフォルトコンストラクタ
	public GenerateAndVerifyJWTToken() {
		
	}
	
	public Response F_GenerateJWTResponse(GenerateJWTResponseCommon generateJWTResponseCommon) {
		GenerateJWTResponseDetails generateJWTResponseDetails = new GenerateJWTResponseDetails();
		
		generateJWTResponseDetails.setStatus(generateJWTResponseCommon.getGenerateJWTResponseStatus());
		generateJWTResponseDetails.setMessage(generateJWTResponseCommon.getGenerateJWTResponseMessage());
		generateJWTResponseDetails.setTime(LocalDateTime.now().toString());
		return Response.status(generateJWTResponseCommon.getStatus()).entity(generateJWTResponseDetails).build();
	}
	
	public Response F_GenerateJWTService(String userId) {
		GenerateJWTResponseDetails generateJWTResponseDetails = new GenerateJWTResponseDetails();
		
		GenerateJWTResponseCommon generateJWTResponseCommon = GenerateJWTResponseCommon.JWTS200;
		StringBuilder jwtToken = new StringBuilder();
		
		Response jwtResponse = F_CheckRequestBody(userId);
		if(jwtResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return jwtResponse;
		}else {
			//Not Execute
		}
		
		jwtResponse = F_GenerateJWT(jwtToken);
		if(jwtResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return jwtResponse;
		}else {
			//Not Execute
		}
		
		jwtResponse = F_VerifyJWT(jwtToken);
		if(jwtResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return jwtResponse;
		}else {
			//Not Execute
		}
		
		jwtResponse = F_CheckUserInfoAndUpdateJWT(userId, jwtToken);
		if(jwtResponse.getStatus() != Response.Status.OK.getStatusCode()) {
			return jwtResponse;
		}else {
			//Not Execute
		}
		
		generateJWTResponseDetails.setStatus(generateJWTResponseCommon.getGenerateJWTResponseStatus());
		generateJWTResponseDetails.setMessage(generateJWTResponseCommon.getGenerateJWTResponseMessage());
		generateJWTResponseDetails.setTime(LocalDateTime.now().toString());
		generateJWTResponseDetails.setToken(jwtToken.toString());
		return Response.status(Response.Status.OK.getStatusCode()).entity(generateJWTResponseDetails).build();
	}
	
	private Response F_CheckRequestBody(String userId) {
		GenerateJWTResponseDetails generateJWTResponseDetails = new GenerateJWTResponseDetails();
		
		if(stringUtil.isNullorEmptytoString(userId)) {
			return F_GenerateJWTResponse(GenerateJWTResponseCommon.JWTE400);
		}else {
			//Not Execute
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generateJWTResponseDetails).build();
	}
	
	private Response F_GenerateJWT(StringBuilder jwtToken) {
		GenerateJWTResponseDetails generateJWTResponseDetails = new GenerateJWTResponseDetails();
		
		try {
		    Date expireTime = new Date();
		    expireTime.setTime(expireTime.getTime() + 600000l);

		    Algorithm algorithm = Algorithm.HMAC256("secret");
		    String token = JWT.create()
		            .withIssuer("auth0")
		            .withExpiresAt(expireTime)
		            .sign(algorithm);
		    jwtToken.append(token);
		} catch (JWTCreationException jwtCreationException){
		    return F_GenerateJWTResponse(GenerateJWTResponseCommon.JWTE500);
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generateJWTResponseDetails).build();
	}
	
	private Response F_VerifyJWT(StringBuilder jwtToken) {
		GenerateJWTResponseDetails generateJWTResponseDetails = new GenerateJWTResponseDetails();
		
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer("auth0")
		            .build();
			DecodedJWT jwt = verifier.verify(jwtToken.toString());
		}catch (JWTVerificationException jwtVerificationException) {
			return F_GenerateJWTResponse(GenerateJWTResponseCommon.JWTE500);
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generateJWTResponseDetails).build();
	}
	
	private Response F_CheckUserInfoAndUpdateJWT(String userId, StringBuilder jwtToken) {
		GenerateJWTResponseDetails generateJWTResponseDetails = new GenerateJWTResponseDetails();
		
		int retryMaxCount = 0;
		
		while(retryMaxCount < 3) {
			try(Connection connection1 = dataSourceHolder.connection(Duration.ofSeconds(12));
					PreparedStatement preparedStatement1 = connection1.prepareStatement(GlobalLogicVariable.SelectSQL)){
				preparedStatement1.setString(1, userId);
				
				try(ResultSet resultSet = preparedStatement1.executeQuery()){
					if(resultSet.next()) {
						try(Connection connection2 = dataSourceHolder.connection(Duration.ofSeconds(12))){
							connection2.setAutoCommit(false);
							try(PreparedStatement preparedStatement2 = connection2.prepareStatement(GlobalLogicVariable.JWTUpdateSQL)){
								preparedStatement2.setString(1, jwtToken.toString());
								preparedStatement2.setString(2, userId);
								preparedStatement2.executeUpdate();
								connection2.commit();
							}
						}
					}else {
						//Not Execute
					}
				}
			} catch (ClassNotFoundException classNotFoundException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_GenerateJWTResponse(GenerateJWTResponseCommon.JWTE500);
				}
			} catch (SQLException sqlException) {
				retryMaxCount ++;
				if(retryMaxCount < 3) {
					continue;
				}else {
					return F_GenerateJWTResponse(GenerateJWTResponseCommon.JWTE500);
				}
			}
			break;
		}
		return Response.status(Response.Status.OK.getStatusCode()).entity(generateJWTResponseDetails).build();
	}
}
