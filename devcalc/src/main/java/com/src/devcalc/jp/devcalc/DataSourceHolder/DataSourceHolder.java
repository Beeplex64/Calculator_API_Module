package com.src.devcalc.jp.devcalc.DataSourceHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

public class DataSourceHolder {
	
	public Connection connection(Duration duration)
	throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/DevelopCalcDB",
				"postgres", "password&ssl=true");
	}
}