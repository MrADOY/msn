package com.msn.chat.payload;

public class ListerMessageRequest {

	protected String emetteur;
	protected String destinataire;
	
	public ListerMessageRequest() {

	}
	
	public ListerMessageRequest(String emetteur, String destinaire) {
		super();
		this.emetteur = emetteur;
		this.destinataire = destinaire;
	}
	
	public String getEmetteur() {
		return emetteur;
	}
	public void setEmetteur(String emetteur) {
		this.emetteur = emetteur;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	
	
}
