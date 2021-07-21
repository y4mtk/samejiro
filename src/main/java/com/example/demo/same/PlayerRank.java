package com.example.demo.same;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="player_rank")
public class PlayerRank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="player_name")
	private String playerName;

	@Column(name="prize")
	private Integer prize;

	@Column(name="game_code")
	private Integer gameCode;

	@Column(name="date")
	private Date date;

	@Transient
	private int rank;

	public PlayerRank(){

	}

	public PlayerRank(String playerName, Integer prize, Integer gameCode, Date date){
		this.playerName = playerName;
		this.prize = prize;
		this.gameCode = gameCode;
		this.date = date;
	}

	public PlayerRank(Integer code, String playerName, Integer prize, Integer gameCode){
		this.code = code;
		this.playerName = playerName;
		this.prize = prize;
		this.gameCode = gameCode;
	}


	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getPrize() {
		return prize;
	}

	public void setPrize(Integer prize) {
		this.prize = prize;
	}

	public Integer getGameCode() {
		return gameCode;
	}

	public void setGameCode(Integer gameCode) {
		this.gameCode = gameCode;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

}
