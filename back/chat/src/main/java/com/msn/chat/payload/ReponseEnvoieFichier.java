package com.msn.chat.payload;

public class ReponseEnvoieFichier {
    private String nomFichier;
    private String URITelechargement;
    private String typeFichier;
    private long taille;
    
	public ReponseEnvoieFichier(String nomFichier, String URITelechargement, String typeFichier, long taille) {
		this.nomFichier = nomFichier;
		this.URITelechargement = URITelechargement;
		this.typeFichier = typeFichier;
		this.taille = taille;
	}
	
	public String getNomFichier() {
		return nomFichier;
	}
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	public String getURITelechargement() {
		return URITelechargement;
	}
	public void setURITelechargement(String uRITelechargement) {
		URITelechargement = uRITelechargement;
	}
	public String getTypeFichier() {
		return typeFichier;
	}
	public void setTypeFichier(String typeFichier) {
		this.typeFichier = typeFichier;
	}
	public long getTaille() {
		return taille;
	}
	public void setTaille(long taille) {
		this.taille = taille;
	}

   
}