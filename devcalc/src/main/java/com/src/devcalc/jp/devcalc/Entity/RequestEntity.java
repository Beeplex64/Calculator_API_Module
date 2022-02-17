package com.src.devcalc.jp.devcalc.Entity;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RequestEntity {
	
	private String userid;
	
	private String password;
	
	private Long phone;
	
	private String profession;
	
	private String mail;
	
	private String age;

	public String getuserid() {
		return userid;
	}

	public void setuserid(String userid) {
		this.userid = userid;
	}

	public String getpassword() {
		return password;
	}

	public void setpassword(String password) {
		this.password = password;
	}

	public Long getphone() {
		return phone;
	}

	public void setphone(Long phone) {
		this.phone = phone;
	}

	public String getprofession() {
		return profession;
	}

	public void setprofession(String profession) {
		this.profession = profession;
	}

	public String getmail() {
		return mail;
	}

	public void setmail(String mail) {
		this.mail = mail;
	}

	public String getage() {
		return age;
	}

	public void setage(String age) {
		this.age = age;
	}
}
