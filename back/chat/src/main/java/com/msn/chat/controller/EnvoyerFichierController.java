package com.msn.chat.controller;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.msn.chat.message.Message;
import com.msn.chat.message.MessageType;
import com.msn.chat.payload.ReponseEnvoieFichier;

@RestController
@RequestMapping("/api/fichier/")
public class EnvoyerFichierController {

    @Autowired
    private GestionEnregistrementFichier serviceSauvegardeLocal;
    
	@PostMapping("/envoyer-fichier")
	public ReponseEnvoieFichier envoyerFichier(@RequestParam("fichier") MultipartFile fichier,
			@RequestParam("emetteur") String emetteur, @RequestParam("destinataire") String destinataire) {
		String nomFichier = serviceSauvegardeLocal.enregistrerFichier(fichier);

		String URIFichierDownload = ServletUriComponentsBuilder.fromCurrentContextPath().path("/telechargerFichier/")
				.path(nomFichier).toUriString();

		Message.send(URIFichierDownload,
				new MessageType(destinataire, emetteur).ampqRoutingKey());
		
		return new ReponseEnvoieFichier(nomFichier, URIFichierDownload, fichier.getContentType(), fichier.getSize());
	}

	@PostMapping("/envoyer-des-fichiers")
	public List<ReponseEnvoieFichier> envoyerPlusieursFichiers(@RequestParam("files") MultipartFile[] fichiers,
			@RequestParam("emetteur") String emetteur, @RequestParam("destinataire") String destinataire) {
		return Arrays.asList(fichiers).stream().map(fichier -> envoyerFichier(fichier, emetteur, destinataire))
				.collect(Collectors.toList());
	}

	@GetMapping("/telecharger-fichier/{nomfichier:.+}")
	public ResponseEntity<Resource> telechargerFichier(@PathVariable String nomfichier, HttpServletRequest request) {
		// On charge le fichier comme une ressouce -> Voir doc Java

		Resource resource = serviceSauvegardeLocal.chargerFichierCommeRessource(nomfichier);

		// On essaye de déterminer le type de contenu du fichier
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			// Retour au type de contenu par défaut si le type n'a pas pu être déterminé
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "piece-jointe; nomfichier=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
