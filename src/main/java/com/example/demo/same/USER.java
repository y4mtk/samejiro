package com.example.demo.same;

public class USER {
	private String ID;
	private String NAME;
	private String PW;

	public USER() {}

	public USER(String ID, String NAME, String PW) {
		this.ID = ID;
		this.NAME = NAME;
		this.PW = PW;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getPW() {
		return PW;
	}

	public void setPW(String pW) {
		PW = pW;
	}
}
