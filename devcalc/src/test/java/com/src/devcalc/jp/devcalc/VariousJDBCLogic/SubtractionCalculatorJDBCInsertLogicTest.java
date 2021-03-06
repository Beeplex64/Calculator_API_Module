package com.src.devcalc.jp.devcalc.VariousJDBCLogic;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.src.devcalc.jp.devcalc.DataSourceHolder.DataSourceHolder;
import com.src.devcalc.jp.devcalc.ResponseContent.CalculatorResponseDetails;

public class SubtractionCalculatorJDBCInsertLogicTest {
	
	@Mock
	Connection connection;
	
	@Mock
	PreparedStatement preparedStatement;
	
	@Mock
	ResultSet resultSet;
	
	@Mock
	DataSourceHolder dataSourceHolder;
	
	@InjectMocks
	SubtractionCalculatorJDBCInsertLogic target = new SubtractionCalculatorJDBCInsertLogic();
	
	@Test
	public void testService_OK() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String symbol = "-";
		final int number1 = 1;
		final int number2 = 2;
		final int result = -1;
		
		Response response = target.F_SubtractionJDBCService(userId, number1, number2, symbol, result);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "S200");
		assertEquals(additionResponseDetails.getMessage(), "SUCCESS");
		assertEquals(additionResponseDetails.getApino(), "3.SubtractionCalcAPI");
	}

	@Test
	public void testService_E400_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "";
		final String symbol = "-";
		final int number1 = 1;
		final int number2 = 2;
		final int result = -1;
		
		Response response = target.F_SubtractionJDBCService(userId, number1, number2, symbol, result);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E400");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "3.SubtractionCalcAPI");
	}
	
	@Test
	public void testService_E400_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "TestUser";
		final String symbol = "";
		final int number1 = 1;
		final int number2 = 2;
		final int result = -1;
		
		Response response = target.F_SubtractionJDBCService(userId, number1, number2, symbol, result);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E400");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "3.SubtractionCalcAPI");
	}
	
	@Test
	public void testService_E500_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenThrow(SQLException.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String symbol = "-";
		final int number1 = 1;
		final int number2 = 2;
		final int result = -1;
		
		Response response = target.F_SubtractionJDBCService(userId, number1, number2, symbol, result);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E500");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "3.SubtractionCalcAPI");
	}
	
	@Test
	public void testService_E500_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenThrow(ClassNotFoundException.class);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String symbol = "-";
		final int number1 = 1;
		final int number2 = 2;
		final int result = -1;
		
		Response response = target.F_SubtractionJDBCService(userId, number1, number2, symbol, result);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E500");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "3.SubtractionCalcAPI");
	}
}
