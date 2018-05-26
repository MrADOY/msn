package com.msn.chat.repertoire;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msn.chat.modeles.Role;
import com.msn.chat.modeles.TypeRole;

/**
 * MicroService de gestion des logins 
 * Auteurs : Aurélien Pietrzak
 */

/**
 * 
 * Repertoire role étend JPARepository qui elle même va étendre PagingAndSortingRepository, 
 * QueryByExampleExecutor,CrudExecutor
 * Sprind Data vous permet de créer des méthodes pour accéder à la volée aux données de la BDD sans effectuer 
 * de requête à la compilation celle-ci va implémenter la méthode par elle même
 * 
 * https://spring.io/guides/gs/accessing-data-jpa/
 * 
 */

public interface RepertoireRole extends JpaRepository<Role, Long>{

	Optional<Role> findByType(TypeRole type);
}
