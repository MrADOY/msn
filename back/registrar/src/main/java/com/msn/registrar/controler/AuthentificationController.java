package com.msn.registrar.controler;


import java.net.URI;
import java.util.Collections;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.msn.registrar.exception.AppException;
import com.msn.registrar.exception.BadRequestException;
import com.msn.registrar.message.Message;
import com.msn.registrar.modeles.Application;
import com.msn.registrar.modeles.Role;
import com.msn.registrar.modeles.TypeLog;
import com.msn.registrar.modeles.TypeRole;
import com.msn.registrar.modeles.Utilisateur;
import com.msn.registrar.payload.ApiResponse;
import com.msn.registrar.payload.AuthenticationResponse;
import com.msn.registrar.payload.ConnexionRequest;
import com.msn.registrar.payload.DeconnexionRequest;
import com.msn.registrar.payload.InscriptionRequest;
import com.msn.registrar.repertoire.RepertoireRole;
import com.msn.registrar.repertoire.RepertoireUtilisateurs;
import com.msn.registrar.securite.JwtFournisseur;
import com.msn.registrar.securite.UtilisateurPrincipal;

@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	RepertoireUtilisateurs repertoireUtilisateur;

	@Autowired
	RepertoireRole repertoireRole;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtFournisseur jwtFournisseur;
	
	/* Uniquement pour tester les appels API */
	@GetMapping("/status")
	public String reponseAPI() {
		return "It's works";
	}
	
	@PostMapping("/connexion")
	public ResponseEntity<?> authentifierUtilisateur(@Valid @RequestBody ConnexionRequest connexionRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					connexionRequest.getEmail(), connexionRequest.getPassword()));
		} catch (AuthenticationException e) {
			Message.sendLogMessage(connexionRequest.getEmail() + "n'a pas su se connecter",
				TypeLog.ERREUR, Application.REGISTRAR);
			throw new BadRequestException(
					"Erreur d'authentification" + connexionRequest.getEmail() + connexionRequest.getPassword());
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtFournisseur.genererToken(authentication);
	
		// Retourne le token et les details de l'utilisateur
		UtilisateurPrincipal util = UtilisateurPrincipal.creer(repertoireUtilisateur
				.findByEmail(connexionRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException(
						"Utilsateur non trouvé avec cette email : " + connexionRequest.getEmail())));

		Message.sendLogMessage(connexionRequest.getEmail() + " s'est connecté",
				TypeLog.INFO, Application.REGISTRAR);
		ConnexionActuellesController.addUser(util);
		return ResponseEntity.ok(new AuthenticationResponse(jwt, util));
	}

	@RequestMapping(path ="/deconnexion", method = RequestMethod.DELETE)
	
	public ResponseEntity<?> deconnexion(@Valid @RequestBody DeconnexionRequest deconnexionRequest) {

		UtilisateurPrincipal util = UtilisateurPrincipal.creer(repertoireUtilisateur
				.findByEmail(deconnexionRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException(
						"Utilsateur non trouvé avec cette email : " + deconnexionRequest.getEmail())));

		ConnexionActuellesController.removeUser(util);
		
		Message.sendLogMessage(deconnexionRequest.getEmail() + "s'est deconnecté",
				TypeLog.INFO, Application.REGISTRAR);
		return ResponseEntity.ok("Deconnexion" + new Date().toString());
	}

	@GetMapping("/detail/{email}")
	public ResponseEntity<?> detail(@PathVariable("email") String email){
		Utilisateur utilisateur = repertoireUtilisateur.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("Utilsateur non trouvé avec cette email : " + email));
		return ResponseEntity.ok(UtilisateurPrincipal.creer(utilisateur));
	}
	

	@PostMapping("/inscription")
	public ResponseEntity<?> inscrireUser(@Valid @RequestBody InscriptionRequest inscriptionRequest) {
		
		if (repertoireUtilisateur.existsByEmail(inscriptionRequest.getEmail())) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Adresse mail déjà utilisée"), HttpStatus.BAD_REQUEST);
		}

		// On créer le compte utilisateur

		Utilisateur utilisateurs = new Utilisateur(inscriptionRequest.getNom(), inscriptionRequest.getPrenom(),
				inscriptionRequest.getEmail(), inscriptionRequest.getPassword());

		utilisateurs.setPassword(passwordEncoder.encode(utilisateurs.getPassword()));

		Role userRole = repertoireRole.findByType(TypeRole.ROLE_USER)
				.orElseThrow(() -> new AppException("Les roles ne sont pas encore définis."));

		utilisateurs.setRoles(Collections.singleton(userRole));

		Utilisateur result = repertoireUtilisateur.save(utilisateurs);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getEmail()).toUri();
		Message.sendLogMessage(inscriptionRequest.getEmail() + "s'est inscrit",
				TypeLog.INFO, Application.REGISTRAR);
		return ResponseEntity.created(location).body(new ApiResponse(true, "Utilisateur correctement enregitré"));
	}
}