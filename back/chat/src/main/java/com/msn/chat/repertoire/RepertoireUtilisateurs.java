package com.msn.chat.repertoire;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msn.chat.modeles.Utilisateur;

/**
 * MicroService de gestion des logins 
 * Auteurs : Aurélien Pietrzak
 */

/**
 * 
 * Repertoire utilisateurs étend JPARepository qui elle même va étendre PagingAndSortingRepository, 
 * QueryByExampleExecutor,CrudExecutor
 * Sprind Data vous permet de créer des méthodes pour accéder à la volée aux données de la BDD sans effectuer 
 * de requête à la compilation celle-ci va implémenter la méthode par elle même
 * 
 * https://spring.io/guides/gs/accessing-data-jpa/
 * 
 */

public interface RepertoireUtilisateurs extends JpaRepository<Utilisateur, Long> {

	
	Optional<Utilisateur> findByEmail(String email);

	List<Utilisateur> findByIdIn(List<Long> userIds);

	Boolean existsByEmail(String email);

}
