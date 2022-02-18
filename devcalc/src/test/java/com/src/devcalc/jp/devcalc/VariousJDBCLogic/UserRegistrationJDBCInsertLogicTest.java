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

public class UserRegistrationJDBCInsertLogicTest {
	
	@Mock
	Connection connection;
	
	@Mock
	PreparedStatement preparedStatement;
	
	@Mock
	ResultSet resultSet;
	
	@Mock
	DataSourceHolder dataSourceHolder;
	
	@InjectMocks
	UserRegistrationJDBCInsertLogic target = new UserRegistrationJDBCInsertLogic();
	
	@Test
	public void testService_OK() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(dataSourceHolder.connection(Duration.ofSeconds(12))).thenReturn(connection);
		Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
		Mockito.when(resultSet.next()).thenReturn(false);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		final String phone = "08000000000";
		final String profession = "Test";
		final String mail = "Test1234@Test.com";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "S201");
		assertEquals(generalResponseDetails.getMessage(), "SUCCESS");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}

	@Test
	public void testService_E400_1() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "";
		final String password = "Test@Test1234";
		final String phone = "08000000000";
		final String profession = "Test";
		final String mail = "Test1234@Test.com";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E400");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E400_2() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "TestUser";
		final String password = "";
		final String phone = "08000000000";
		final String profession = "Test";
		final String mail = "Test1234@Test.com";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E400");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E400_3() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		final String phone = "";
		final String profession = "Test";
		final String mail = "Test1234@Test.com";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E400");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E400_4() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		final String phone = "08000000000";
		final String profession = "";
		final String mail = "Test1234@Test.com";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E400");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
	
	@Test
	public void testService_E400_5() throws SQLException, ClassNotFoundException {
		MockitoAnnotations.initMocks(this);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		final String phone = "08000000000";
		final String profession = "Test";
		final String mail = "";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E400");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
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
		final String password = "Test@Test1234";
		final String phone = "08000000000";
		final String profession = "Test";
		final String mail = "Test1234@Test.com";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
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
		Mockito.when(resultSet.next()).thenReturn(true);
		
		final String userId = "TestUser";
		final String password = "Test@Test1234";
		final String phone = "08000000000";
		final String profession = "Test";
		final String mail = "Test1234@Test.com";
		final int age = 100;
		
		Response response = target.F_RegistJDBCService(userId, password, phone, profession, mail, age);
		GeneralResponseDetails generalResponseDetails = (GeneralResponseDetails) response.getEntity();
		assertEquals(generalResponseDetails.getStatus(), "E500");
		assertEquals(generalResponseDetails.getMessage(), "システムエラーが発生しました。管理者に問い合わせてください。");
		assertEquals(generalResponseDetails.getApino(), "5.UserRegistrationAPI");
	}
}
