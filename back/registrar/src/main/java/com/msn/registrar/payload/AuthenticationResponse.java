package com.msn.registrar.payload;

import com.msn.registrar.securite.UtilisateurPrincipal;

public class AuthenticationResponse {

	private String token;
	private String tokenType = "Bearer";
	private UtilisateurPrincipal utilisateur;

	public AuthenticationResponse(String token,UtilisateurPrincipal utilisateur) {
		this.token = token;
		this.utilisateur = utilisateur;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UtilisateurPrincipal getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(UtilisateurPrincipal utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
