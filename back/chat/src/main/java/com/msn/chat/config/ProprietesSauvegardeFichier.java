package com.msn.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/* Permet de configurer avec les propriétes du fichier application.properties */

@ConfigurationProperties(prefix = "fichier")
public class ProprietesSauvegardeFichier {
    
	private String dossierUpload;

	public String getDossierUpload() {
		return dossierUpload;
	}

	public void setDossierUpload(String dossierUpload) {
		this.dossierUpload = dossierUpload;
	}

   
}
