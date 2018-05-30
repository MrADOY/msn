package com.msn.chat.modeles;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Type;

/* Class qui permet l'enregistrement des logs dans la BDD */

@Entity
@Table(name = "log")
public class Log {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Enumerated(EnumType.STRING)
    private TypeLog type;

	@Enumerated(EnumType.STRING)
    private Application appli;

    @NotBlank
    @Column(length = 30000)
    private String message;
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    
	public Log(TypeLog type, Application appli,String message, Date date) {
		this.appli = appli;
		this.type = type;
		this.message = message;
		this.date = date;
	}

	@Override
	public String toString() {
		return "Log [type=" + type + ", appli=" + appli + ", message=" + message + ", date=" + date + "]";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TypeLog getType() {
		return type;
	}

	public void setType(TypeLog type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public Application getAppli() {
		return appli;
	}

	public void setAppli(Application appli) {
		this.appli = appli;
	}
    
    
}
