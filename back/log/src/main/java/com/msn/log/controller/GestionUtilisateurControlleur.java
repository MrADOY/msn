package com.msn.log.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.msn.log.exception.AppException;
import com.msn.log.exception.BadRequestException;
import com.msn.log.message.Message;
import com.msn.log.modeles.Application;
import com.msn.log.modeles.Role;
import com.msn.log.modeles.TypeLog;
import com.msn.log.modeles.Utilisateur;
import com.msn.log.payload.ApiResponse;
import com.msn.log.payload.DeleteRequest;
import com.msn.log.payload.InscriptionRequest;
import com.msn.log.payload.UpdateRequest;
import com.msn.log.payload.UtilisateursReponse;
import com.msn.log.repertoire.RepertoireRole;
import com.msn.log.repertoire.RepertoireUtilisateurs;
import com.msn.log.repertoire.RepertoiresLog;
import com.msn.log.securite.UtilisateurPrincipal;

@RestController
@RequestMapping("api/admin/")
public class GestionUtilisateurControlleur {

	@Autowired
	RepertoireRole repertoireRole;

	@Autowired
	RepertoireUtilisateurs repertoireUtilisateur;

	@Autowired
	RepertoiresLog repertoireLog;

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

	@PostMapping("/inscription-admin")
	public ResponseEntity<?> inscrireUser(@Valid @RequestBody InscriptionRequest inscriptionRequest) {

		if (repertoireUtilisateur.existsByEmail(inscriptionRequest.getEmail())) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Adresse mail déjà utilisée"),
					HttpStatus.BAD_REQUEST);
		}

		// On créer le compte utilisateur

		Utilisateur utilisateurs = new Utilisateur(inscriptionRequest.getNom(), inscriptionRequest.getPrenom(),
				inscriptionRequest.getEmail(), inscriptionRequest.getPassword());

		utilisateurs.setPassword(passwordEncoder.encode(utilisateurs.getPassword()));

		Role userRole = repertoireRole.findByType(com.msn.log.modeles.TypeRole.ROLE_ADMIN)
				.orElseThrow(() -> new AppException("Les roles ne sont pas encore définis."));

		utilisateurs.setRoles(Collections.singleton(userRole));

		Utilisateur result = repertoireUtilisateur.save(utilisateurs);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getEmail()).toUri();
		Message.sendLogMessage(inscriptionRequest.getEmail() + " s'est inscrit en ADMIN", TypeLog.INFO,
				Application.REGISTRAR);
		return ResponseEntity.created(location).body(new ApiResponse(true, "Utilisateur correctement enregitré"));
	}

	@GetMapping("/log-pdf")
	public ResponseEntity<?> genererLogPDF() {
		PDDocument document;

		document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		PDDocumentInformation pdd = document.getDocumentInformation();

		pdd.setAuthor("MSN ADMIN");
		pdd.setTitle("LOG");
		pdd.setCreator("Systeme");
		pdd.setSubject("Log des applications");

		page = document.getPage(0);
		PDPageContentStream contentStream;
		try {
			contentStream = new PDPageContentStream(document, page);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
			contentStream.setLeading(14.5f);
			contentStream.newLineAtOffset(25, 700);
			
			// On limite les logs à 20 et les tri par ordre decroissant
			repertoireLog.findAll().stream().limit(20).sorted((a, b) -> b.getDate().compareTo(a.getDate()))
					.forEach(l -> {
						try {
							contentStream.showText(l.toString());
							contentStream.newLine();
						} catch (IOException e) {

						}
					});
			contentStream.endText();
			contentStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			document.save("test.pdf");
			document.close();
		} catch (IOException e) {
			System.out.println("Impossible de refermer le pdf");
		}
		return null;
	}
}
