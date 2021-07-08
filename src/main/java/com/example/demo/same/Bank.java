package com.example.demo.same;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bank")
public class Bank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="user_code")
	private Integer user_code;

	@Column(name="money")
	private int money;

	@Column(name="lost")
	private int lost;

	@Column(name="won")
	private int won;

	public Bank(Integer user_code, int money, int lost, int won) {
		this.user_code = user_code;
		this.money = money;
		this.lost = lost;
		this.won = won;
	}



	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getUser_code() {
		return user_code;
	}

	public void setUser_code(Integer user_code) {
		this.user_code = user_code;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getLost() {
		return lost;
	}

	public void setLost(int lost) {
		this.lost = lost;
	}

	public int getWon() {
		return won;
	}

	public void setWon(int won) {
		this.won = won;
	}
}
