package com.msn.log.message;

public class MessageType {

	private String emmetteur;
	private String destinaire;

	public MessageType(String dest, String emetteur){
		this.emmetteur = emetteur;
		this.destinaire = dest;
	}

	public String ampqRoutingKey() {
		return this.destinaire + "." + this.emmetteur;
	}
	
	public String getEmmetteur() {
		return emmetteur;
	}

	public void setEmmetteur(String emmetteur) {
		this.emmetteur = emmetteur;
	}

	public String getDestinaire() {
		return destinaire;
	}

	public void setDestinaire(String destinaire) {
		this.destinaire = destinaire;
	}
	
	
	
	
	
}
