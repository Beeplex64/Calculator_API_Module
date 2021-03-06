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

public class AdditionCalculatorJDBCSelectLogicTest {
	
	@Mock
	Connection connection;
	
	@Mock
	PreparedStatement preparedStatement;
	
	@Mock
	ResultSet resultSet;
	
	@Mock
	DataSourceHolder dataSourceHolder;
	
	@InjectMocks
	AdditionCalculatorJDBCSelectLogic target = new AdditionCalculatorJDBCSelectLogic();
	
	@Test
	public void testService_OK() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		
		Response response = target.F_AdditionJDBCService(userId);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "S200");
		assertEquals(additionResponseDetails.getMessage(), "SUCCESS");
		assertEquals(additionResponseDetails.getApino(), "1.AdditionCalcAPI");
	}

	@Test
	public void testService_E400() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "";
		
		Response response = target.F_AdditionJDBCService(userId);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E400");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "1.AdditionCalcAPI");
	}
	
	@Test
	public void testService_E404() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(false);
		
		final String userId = "TestUser";
		
		Response response = target.F_AdditionJDBCService(userId);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E404");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "1.AdditionCalcAPI");
	}
	
	@Test
	public void testService_E500_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenThrow(SQLException.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		
		Response response = target.F_AdditionJDBCService(userId);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E500");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "1.AdditionCalcAPI");
	}
	
	@Test
	public void testService_E400_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenThrow(ClassNotFoundException.class);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		
		Response response = target.F_AdditionJDBCService(userId);
		CalculatorResponseDetails additionResponseDetails = (CalculatorResponseDetails) response.getEntity();
		assertEquals(additionResponseDetails.getStatus(), "E500");
		assertEquals(additionResponseDetails.getMessage(), "??????????????????????????????????????????????????????????????????????????????????????????");
		assertEquals(additionResponseDetails.getApino(), "1.AdditionCalcAPI");
	}
}
