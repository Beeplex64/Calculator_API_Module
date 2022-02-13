package com.src.devcalc.jp.devcalc.GlobalVariable;

public class GlobalLogicVariable {

	//CalculatorSQL
	public static String SelectSQL = "SELECT * FROM T_USERINFO WHERE C_USERID = ? FOR UPDATE NOWAIT";
	public static String InsertSQL = "INSERT INTO T_CALCRESULT (C_USERID, C_NUM1, C_NUM2, C_SYMBOL, C_RESULT) VALUES(?,?,?,?,?)";
	//CalculatorSymbol
	public static String additionSymbol = "+";
	public static String subtractionSymbol = "-";
	public static String multiplicationSymbol = "×";
	public static String divisionSymbol = "÷";
}
