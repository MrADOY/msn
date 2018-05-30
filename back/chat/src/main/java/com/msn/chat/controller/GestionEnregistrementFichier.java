package com.msn.chat.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.msn.chat.config.ProprietesSauvegardeFichier;
import com.msn.chat.exception.FileStorageException;
import com.msn.chat.exception.MyFileNotFoundException;
import com.msn.chat.message.Message;
import com.msn.chat.modeles.TypeLog;

@Service
public class GestionEnregistrementFichier {

    private final Path LocalisationFichierSauvegarde;

    @Autowired
    public GestionEnregistrementFichier(ProprietesSauvegardeFichier proprietesSauvegardeFichier) {
    		
        this.LocalisationFichierSauvegarde = Paths.get(proprietesSauvegardeFichier.getDossierUpload())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.LocalisationFichierSauvegarde);
        } catch (Exception e) {
            throw new FileStorageException("Impossible de créer de dossier", e);
        }
    }

	public String enregistrerFichier(MultipartFile fichier) {

		// On met aux normes le PATH
		String nomFichier = StringUtils.cleanPath(fichier.getOriginalFilename());

		try {
			// On copie le fichier dans le dossier (Attention Remplace le fichier si il
			// existe deja)

			Path localisation = this.LocalisationFichierSauvegarde.resolve(nomFichier);
			Files.copy(fichier.getInputStream(), localisation, StandardCopyOption.REPLACE_EXISTING);

			return nomFichier;
		} catch (IOException e) {
			throw new FileStorageException("Impossible d'enregistrer le fichier " + nomFichier, e);
		}
	}

	public Resource chargerFichierCommeRessource(String nomFichier) {
		try {
			Path filePath = this.LocalisationFichierSauvegarde.resolve(nomFichier).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + nomFichier);
			}
		} catch (MalformedURLException e) {
			throw new MyFileNotFoundException("File not found " + nomFichier, e);
		}
	}
}