package br.com.suricattus.surispring.jsf.primefaces.component.captcha;

import java.io.Serializable;

public class Verification implements Serializable {

	private static final long serialVersionUID = 1L;

	private String challenge;
	private String answer;
	
	public Verification() {
		//NoOp
	}
	
	public Verification(String challenge, String answer) {
		this.challenge = challenge;
		this.answer = answer;
	}
	
	public String getChallenge() {
		return challenge;
	}
	
	public String getAnswer() {
		return answer;
	}
}