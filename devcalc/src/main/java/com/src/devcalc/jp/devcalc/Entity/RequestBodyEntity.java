package com.src.devcalc.jp.devcalc.Entity;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.HeaderParam;

@RequestScoped
public class RequestBodyEntity {
	
	@HeaderParam("userid")
	private String userid;
	
	@HeaderParam("password")
	private String password;
	
	@HeaderParam("phone")
	private Long phone;
	
	@HeaderParam("profession")
	private String profession;
	
	@HeaderParam("mail")
	private String mail;
	
	@HeaderParam("age")
	private Integer age;
	
	@HeaderParam("num1")
	private Integer num1;
	
	@HeaderParam("num2")
	private Integer num2;
	
	public void setuserid(String userid) {
		this.userid = userid;
	}
	
	public String getuserid() {
		return this.userid;
	}
	
	public void setpassword(String password) {
		this.password = password;
	}
	
	public String getpassword() {
		return this.password;
	}
	
	public void setphne(Long phone) {
		this.phone = phone;
	}
	
	public Long getphone() {
		return this.phone;
	}
	
	public void setprofession(String profession) {
		this.profession = profession;
	}
	
	public String getprofession() {
		return this.profession;
	}
	
	public void setmail(String mail) {
		this.mail = mail;
	}
	
	public String getmail() {
		return this.mail;
	}
	
	public void setage(Integer age) {
		this.age = age;
	}
	
	public Integer getage() {
		return this.age;
	}
	
	public void setnum1(Integer num1) {
		this.num1 = num1;
	}
	
	public Integer getnum1() {
		return this.num1;
	}
	
	public void setnum2(Integer num2) {
		this.num2 = num2;
	}
	
	public Integer getnum2() {
		return this.num2;
	}

}
