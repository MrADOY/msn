package com.msn.registrar.controler;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msn.registrar.message.Message;
import com.msn.registrar.message.MessageType;
import com.msn.registrar.securite.UtilisateurPrincipal;

@RestController
@RequestMapping("/api/connexions")
public class ConnexionActuellesController {

	protected static Set<UtilisateurPrincipal> utilisateursConnectes;

	@GetMapping("utilisateurs-co")
	public ResponseEntity<?> findAllUser() {
		if (utilisateursConnectes == null) {
			utilisateursConnectes = new HashSet<>();
		}
		return ResponseEntity.ok(utilisateursConnectes.stream().collect(Collectors.toList()));
	}

	@GetMapping("/connecte/{email}")
	public ResponseEntity<?> isConnected(@PathVariable("email") String email) {
		return ResponseEntity
				.ok(utilisateursConnectes.stream().filter(u -> u.getEmail().equals(email))
						.findFirst().isPresent());
	}

	public static void addUser(UtilisateurPrincipal utilisateur) {
		if (utilisateursConnectes == null) {
			utilisateursConnectes = new HashSet<>();
		}
		/* Envoie une notification à tous les utilisateurs connectés
		 *  d'une personne vient de se connecter
		 */
		if (utilisateursConnectes.add(utilisateur)) {
			utilisateursConnectes.forEach(u -> Message.send(utilisateur.getEmail() + ":" + "s'est connecté",
					new MessageType(u.getEmail(), "service-registrar").ampqRoutingKey()));
			}
		}
	
	public static void removeUser(UtilisateurPrincipal utilisateur) {
		if (utilisateursConnectes == null) {
			utilisateursConnectes = new HashSet<>();
		}
		/* Envoie une notification à tous les utilisateurs connectés
		 *  d'une personne vient de se déconnecter
		 */
		if (utilisateursConnectes.remove(utilisateur)) {
			utilisateursConnectes.forEach(u -> Message.send(utilisateur.getEmail() + ":" + "s'est deconnecté",
					new MessageType(u.getEmail(), "service-registrar").ampqRoutingKey()));
		}
	}
}
	
	
