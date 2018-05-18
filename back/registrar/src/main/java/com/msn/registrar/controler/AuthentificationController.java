package com.msn.registrar.controler;


import com.msn.registrar.exception.AppException;
import com.msn.registrar.exception.BadRequestException;
import com.msn.registrar.message.Message;
import com.msn.registrar.message.MessageType;
import com.msn.registrar.modeles.Role;
import com.msn.registrar.modeles.TypeRole;
import com.msn.registrar.modeles.Utilisateurs;
import com.msn.registrar.payload.ApiResponse;
import com.msn.registrar.payload.ConnexionRequest;
import com.msn.registrar.payload.InscriptionRequest;
import com.msn.registrar.payload.JwtAuthenticationResponse;
import com.msn.registrar.repertoire.RepertoireRole;
import com.msn.registrar.repertoire.RepertoireUtilisateurs;
import com.msn.registrar.securite.JwtFournisseur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

import org.springframework.security.core.AuthenticationException;

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

	@PostMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	
	@PostMapping("/connexion")
	public ResponseEntity<?> authentifierUtilisateur(@Valid @RequestBody ConnexionRequest connexionRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					connexionRequest.getEmail(), connexionRequest.getPassword()));
		} catch (AuthenticationException e) {
			throw new BadRequestException(
					"NOOB THOMAS BUCHARD" + connexionRequest.getEmail() + connexionRequest.getPassword());
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtFournisseur.genererToken(authentication);
		Message.send(connexionRequest.getEmail()+":"+jwt, MessageType.LOGIN);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/inscription")
	public ResponseEntity<?> inscrireUser(@Valid @RequestBody InscriptionRequest inscriptionRequest) {
		
		if (repertoireUtilisateur.existsByEmail(inscriptionRequest.getEmail())) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Adresse mail déjà utilisée"), HttpStatus.BAD_REQUEST);
		}

		// On créer le compte utilisateur

		Utilisateurs utilisateurs = new Utilisateurs(inscriptionRequest.getNom(), inscriptionRequest.getPrenom(),
				inscriptionRequest.getEmail(), inscriptionRequest.getPassword());

		utilisateurs.setPassword(passwordEncoder.encode(utilisateurs.getPassword()));

		Role userRole = repertoireRole.findByType(TypeRole.ROLE_USER)
				.orElseThrow(() -> new AppException("Les roles ne sont pas encore définis."));

		utilisateurs.setRoles(Collections.singleton(userRole));

		Utilisateurs result = repertoireUtilisateur.save(utilisateurs);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getEmail()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Utilisateur correctement enregitré"));
	}
}