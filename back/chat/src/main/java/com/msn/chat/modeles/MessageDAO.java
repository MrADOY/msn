package com.msn.chat.modeles;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "messages")
public class MessageDAO {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	protected String destinataire;

	@NotBlank
	protected String emetteur;

	@NotBlank
	@Column(length = 30000)
	protected String message;
	
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;
	public MessageDAO(String destinataire, String emetteur, String message,Date date) {
		this.destinataire = destinataire;
		this.emetteur = emetteur;
		this.message = message;
		this.date = date;
	}

	public MessageDAO() {
		// vide pour jackson
	}
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public java.util.Date getDate() {
		return date;
	}


	public void setDate(java.util.Date date) {
		this.date = date;
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
