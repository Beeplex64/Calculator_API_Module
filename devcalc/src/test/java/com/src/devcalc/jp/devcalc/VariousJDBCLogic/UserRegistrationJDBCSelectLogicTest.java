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
import com.src.devcalc.jp.devcalc.ResponseContent.GeneralResponseDetails;

public class UserRegistrationJDBCSelectLogicTest {
	
	@Mock
	Connection connection;
	
	@Mock
	PreparedStatement preparedStatement;
	
	@Mock
	ResultSet resultSet;
	
	@Mock
	DataSourceHolder dataSourceHolder;
	
	@InjectMocks
	UserRegistrationJDBCSelectLogic target = new UserRegistrationJDBCSelectLogic();
	
	@Test
	public void testService_OK() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(false);
		
		final String userId = "TestUser";
		
		Response response = target.F_UserRegistJDBC(userId);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "S201");
		assertEquals(generalResponseDetails.getMessage(), "SUCCESS");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E400() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "";
		
		Response response = target.F_UserRegistJDBC(userId);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E400");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E409() throws SQLException, ClassNotFoundException{
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		
		Response response = target.F_UserRegistJDBC(userId);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E409");
		assertEquals(generalResponseDetails.getMessage(), "既にそのユーザーIDは利用されています。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E500_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenThrow(SQLException.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(false);
		
		final String userId = "TestUser";
		
		Response response = target.F_UserRegistJDBC(userId);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E500");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E500_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenThrow(ClassNotFoundException.class);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(false);
		
		final String userId = "TestUser";
		
		Response response = target.F_UserRegistJDBC(userId);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E500");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
}
