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
import com.src.devcalc.jp.devcalc.ResponseContent.UserLoginResponseDetails;

public class UserLoginJDBCSelectLogicTest {
	
	@Mock
	Connection connection;
	
	@Mock
	PreparedStatement preparedStatement;
	
	@Mock
	ResultSet resultSet;
	
	@Mock
	DataSourceHolder dataSourceHolder;
	
	@InjectMocks
	UserLoginJDBCSelectLogic target = new UserLoginJDBCSelectLogic();

	@Test
	public void testService_OK() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.getInt("C_DELETE_FLG")).thenReturn(0);
		Mockito.when(resultSet.getString("C_PASSWORD")).thenReturn("50796e8490a4b170d1c5feeb8d64abd8879396c29b840ffc84206da335ce8169");
		Mockito.when(resultSet.next()).thenReturn(true);
		
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "S200");
		assertEquals(userLoginResponseDetails.getMessage(), "SUCCESS");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
	
	@Test
	public void TestService_E400_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "";
		final String password = "Test@Test1234";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "E400");
		assertEquals(userLoginResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
	
	@Test
	public void testService_E400_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "TestUser";
		final String password = "";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "E400");
		assertEquals(userLoginResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
	
	@Test
	public void testService_E401() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.getInt("C_DELETE_FLG")).thenReturn(1);
		Mockito.when(resultSet.getString("C_PASSWORD")).thenReturn("50796e8490a4b170d1c5feeb8d64abd8879396c29b840ffc84206da335ce8169");
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "E401");
		assertEquals(userLoginResponseDetails.getMessage(), "ユーザーのログイン権限がありません。");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
	
	@Test
	public void testService_E404_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.getInt("C_DELETE_FLG")).thenReturn(0);
		Mockito.when(resultSet.getString("C_PASSWORD")).thenReturn("50796e8490a4b170d1c5feeb8d64abd8879396c29b840ffc84206da335ce8169");
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String password = "50796e8490a4b170d1c5feeb8d64abd8879396c29b840ffc84206da335ce8169";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "E404");
		assertEquals(userLoginResponseDetails.getMessage(), "ユーザーID・パスワードが間違っています。");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
	
	@Test
	public void testService_E404_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.getInt("C_DELETE_FLG")).thenReturn(0);
		Mockito.when(resultSet.getString("C_PASSWORD")).thenReturn("50796e8490a4b170d1c5feeb8d64abd8879396c29b840ffc84206da335ce8169");
		Mockito.when(resultSet.next()).thenReturn(false);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "E404");
		assertEquals(userLoginResponseDetails.getMessage(), "ユーザーID・パスワードが間違っています。");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
	
	@Test
	public void testService_E500_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenThrow(SQLException.class);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.getInt("C_DELETE_FLG")).thenReturn(0);
		Mockito.when(resultSet.getString("C_PASSWORD")).thenReturn("50796e8490a4b170d1c5feeb8d64abd8879396c29b840ffc84206da335ce8169");
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "E500");
		assertEquals(userLoginResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
	
	@Test
	public void testService_E500_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenThrow(ClassNotFoundException.class);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.getInt("C_DELETE_FLG")).thenReturn(0);
		Mockito.when(resultSet.getString("C_PASSWORD")).thenReturn("50796e8490a4b170d1c5feeb8d64abd8879396c29b840ffc84206da335ce8169");
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		
		Response response = target.F_UserLoginJDBC(userId, password);
		UserLoginResponseDetails userLoginResponseDetails = (UserLoginResponseDetails) response.getEntity();
		assertEquals(userLoginResponseDetails.getStatus(), "E500");
		assertEquals(userLoginResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(userLoginResponseDetails.getApino(), "6.LoginAPI");
	}
}
