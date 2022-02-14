package com.src.devcalc.jp.devcalc.VerificationJWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.src.devcalc.jp.devcalc.GlobalVariable.GlobalLogicVariable;

public class VerificationJWTToken {

	static String SuccessResponse = GlobalLogicVariable.verificationSuccessResponse;
	public static String VerifierJWTToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer("auth0")
					.build();
			DecodedJWT jwt = verifier.verify(token);
		}catch(JWTVerificationException jwtVerificationException) {
			return GlobalLogicVariable.verificationJWTVerificationExceptionResponse;
		}
		return SuccessResponse;
	}
}
