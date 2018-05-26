package com.msn.chat.modeles;

import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * MicroService de gestion des logins 
 * Auteurs : Aurélien Pietrzak
 */

/**
 * 
 * @author aurelienpietrzak
 * Le modèle ci-dessous représente un pattern pour JPA 
 * Celui-ci va permettre la persistance dans une BDD 
 * Si la table n'éxiste pas, elle sera creé automatiquement
 */

@Entity
@Table(name = "utilisateurs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "email"
        })
})

public class Utilisateur {

	/**
	 * JPA Génére automatiquement les ID selon la politique choisie
	 */
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String nom;

    @NotBlank
    @Size(max = 40)
    private String prenom;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_utilisateurs",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    
	private Set<Role> roles = new HashSet<>();

	public Utilisateur() {
		//
	}

	public Utilisateur(String nom, String prenom, String email, String password) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
}
