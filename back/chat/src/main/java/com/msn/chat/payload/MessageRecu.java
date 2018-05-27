package com.msn.chat.payload;

public class MessageRecu {

	protected String destinataire;

	protected String emetteur;

	protected String message;

	
	public MessageRecu(String destinataire, String emetteur, String message) {
		this.destinataire = destinataire;
		this.emetteur = emetteur;
		this.message = message;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(String emetteur) {
		this.emetteur = emetteur;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
