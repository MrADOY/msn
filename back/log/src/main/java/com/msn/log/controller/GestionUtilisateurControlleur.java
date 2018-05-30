package com.msn.log.controller;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msn.log.exception.BadRequestException;
import com.msn.log.modeles.Utilisateur;
import com.msn.log.payload.DeleteRequest;
import com.msn.log.payload.UpdateRequest;
import com.msn.log.payload.UtilisateursReponse;
import com.msn.log.repertoire.RepertoireUtilisateurs;
import com.msn.log.securite.UtilisateurPrincipal;

@RestController
@RequestMapping("api/admin/")
public class GestionUtilisateurControlleur {

	@Autowired
	RepertoireUtilisateurs repertoireUtilisateur;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/update")
	public ResponseEntity<?> updateUtilisateur(@Valid @RequestBody UpdateRequest request) {

		Utilisateur utilisateur = null;
		try {
			utilisateur = repertoireUtilisateur.findByEmail(request.getEmail()).get();
		} catch (NoSuchElementException e) {
			throw new BadRequestException("Erreur l'utilisateur n'existe pas" + request.getEmail());
		}
		utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
		repertoireUtilisateur.save(utilisateur);

		return ResponseEntity.ok(UtilisateurPrincipal.creer(utilisateur));
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> deleteUtilisateur(@Valid @RequestBody DeleteRequest request) {

		Utilisateur utilisateur = null;
		try {
			utilisateur = repertoireUtilisateur.findByEmail(request.getEmail()).get();
		} catch (NoSuchElementException e) {
			throw new BadRequestException("Erreur l'utilisateur n'existe pas" + request.getEmail());
		}
		
		repertoireUtilisateur.delete(utilisateur);
		return ResponseEntity.ok("Utilisateur correctement supprimé");
	}
	
	@GetMapping("/lister-utilisateurs")
	public ResponseEntity<?> listerUtilisateurs() {
		return ResponseEntity.ok(repertoireUtilisateur.findAll().stream()
				.map(u -> new UtilisateursReponse(u.getId(), u.getNom(), u.getPrenom(), u.getEmail(), u.getRoles()))
				.collect(Collectors.toList()));
	}
	
}
