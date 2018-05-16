package com.msn.registrar.repertoire;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.msn.registrar.modeles.Utilisateurs;

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

public interface RepertoireUtilisateurs extends JpaRepository<Utilisateurs, Long> {

	
	Optional<Utilisateurs> findByEmail(String email);

	List<Utilisateurs> findByIdIn(List<Long> userIds);

	Boolean existsByEmail(String email);

}
