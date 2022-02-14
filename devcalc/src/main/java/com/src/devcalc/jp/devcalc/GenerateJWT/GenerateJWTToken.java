package com.src.devcalc.jp.devcalc.GenerateJWT;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;

public class GenerateJWTToken {
	
	public static String getJWTToken() {
		String token = new String();
		try {
			Date expireTime = new Date();
			expireTime.setTime(expireTime.getTime() + 600000l);
			
			Algorithm algorithm = Algorithm.HMAC256("secret");
			token = JWT.create()
					.withIssuer("auth0")
					.withExpiresAt(expireTime)
					.sign(algorithm);
		}catch(JWTCreationException jwtCreationException) {
			return GlobalLogicVariable.generateJWTCreationExceptionResponse;
		}
		return token;
	}
	

}
