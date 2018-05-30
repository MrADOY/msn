package com.msn.log.payload;

import java.util.HashSet;
import java.util.Set;

import com.msn.log.modeles.Role;

public class UtilisateursReponse {
	
    private Long id;
    private String nom;
    private String prenom;
    private String email;
	private Set<Role> roles = new HashSet<>();


	public UtilisateursReponse(Long id, String nom, String prenom, String email, Set<Role> roles) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.roles = roles;
	}


	public UtilisateursReponse() {
		
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	

}
