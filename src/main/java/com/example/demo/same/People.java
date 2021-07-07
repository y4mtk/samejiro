package com.example.demo.same;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="people")
public class People {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int code;
	private String userid;
	private String name;
	private String email;
	private String password;

	//デフォルトコンストラクタ
	public People() {

	}

	public People(String userid, String name, String email,String password) {
		this.userid = userid;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public People(String userid, String name,String password) {
		this.userid = userid;
		this.name = name;
		this.password = password;
	}

	//コンストラクター
	public People(int code,String userid, String name, String email,String password) {
		this.code = code;
		this.userid = userid;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



}
