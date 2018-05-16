package com.msn.registrar.securite;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msn.registrar.modeles.Utilisateurs;
import com.msn.registrar.repertoire.RepertoireUtilisateurs;

@Service
public class UtilsateurPrincipalServiceImpl implements UserDetailsService  {

	@Autowired
    RepertoireUtilisateurs repertoireUtilisateur;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Let people login with either username or email
		Utilisateurs utilisateur = repertoireUtilisateur.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("Utilsateur non trouvé avec cette email : " + email));

		return UtilisateurPrincipal.creer(utilisateur);
	}
	
	// This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
    	Utilisateurs utilisateur = repertoireUtilisateur.findById(id).orElseThrow(
            () -> new UsernameNotFoundException("Utilsateur non trouvé avec cette id : " + id)
        );

        return UtilisateurPrincipal.creer(utilisateur);
    }

}
