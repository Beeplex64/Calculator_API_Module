package com.src.devcalc.jp.devcalc.Entity;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RequestEntity {
	
	private String userid;
	
	private String password;
	
	private Integer phone;
	
	private String profession;
	
	private String mail;

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

	public Integer getphone() {
		return phone;
	}

	public void setphone(Integer phone) {
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
	
	

}
