package com.msn.chat.payload;

import javax.validation.constraints.NotBlank;

public class SendMessageRequest {

    @NotBlank
    protected String destinataire;
    
    @NotBlank
    protected String emetteur;
    
    @NotBlank
    protected String message;

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
