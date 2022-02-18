package com.src.devcalc.jp.devcalc.GlobalVariable;

public class GlobalLogicVariable {

	public static String SelectSQL = "SELECT * FROM T_USERINFO WHERE C_USERID = ? FOR UPDATE NOWAIT";
	public static String InsertSQL = "INSERT INTO T_CALCRESULT (C_USERID, C_NUM1, C_NUM2, C_SYMBOL, C_RESULT) VALUES(?,?,?,?,?)";
	public static String additionSymbol = "+";
	public static String subtractionSymbol = "-";
	public static String multiplicationSymbol = "×";
	public static String divisionSymbol = "÷";
	public static String RegistInsertSQL = "INSERT INTO T_USERINFO (C_USERID, C_PHONE, C_PROFESSION, C_AGE, C_DELETE_FLG, C_LASTOPERATION, C_REGISTRATIONDATE, C_PASSWORD, C_LOGINDATE, C_LOGOUTDATE, C_JWTTOKEN, C_ADMINDELETE_FLG, C_MAIL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String JWTUpdateSQL = "UPDATE T_USERINFO SET C_JWTTOKEN = ? WHERE C_USERID = ?";
	public static String UserDeleteSQL = "UPDATE T_USERINFO SET C_DELETE_FLG = ?, C_DELETEDATE = ? WHERE C_USERID = ?";
	public static String UserRequestSQL = "INSERT INTO T_USERREQUEST (C_USERID, C_REQUEST, C_REGISTDATE, C_PRIORITY) VALUES(?,?,?,?)";
	public static String UserInquirySQL = "INSERT INTO T_INQUIRY (C_USERID, C_MAIL, C_INQUIRYCONTENT, C_INQUIRYDATE, C_PRIORITY) VALUES(?,?,?,?,?)";
}