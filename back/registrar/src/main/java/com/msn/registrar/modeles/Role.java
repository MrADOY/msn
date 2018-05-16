package com.msn.registrar.modeles;

/**
 * MicroService de gestion des logins 
 * Auteurs : Aurélien Pietrzak
 */

import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

/**
 * 
 * @author aurelienpietrzak
 * Le modèle ci-dessous représente un pattern pour JPA 
 * Celui-ci va permettre la persistance dans une BDD 
 * Si la table n'éxiste pas, elle sera creé automatiquement
 */

@Entity
@Table(name = "roles")
public class Role {
	
	/**
	 * JPA Génére automatiquement les ID selon la politique choisie
	 */
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private TypeRole type;

    public Role() {

    }

    public Role(TypeRole type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeRole getType() {
        return type;
    }

    public void setType(TypeRole type) {
        this.type = type;
    }
}