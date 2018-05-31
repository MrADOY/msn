package com.msn.chat.repertoire;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msn.chat.modeles.MessageDAO;

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
public interface RepertoireMessage extends JpaRepository<MessageDAO, Long>{

	// Jpa s'adapte au nom de la methode pour générer la requete SQL
	 List<MessageDAO> findAllByEmetteurAndDestinataire(String emetteur,String destinataire);
}
