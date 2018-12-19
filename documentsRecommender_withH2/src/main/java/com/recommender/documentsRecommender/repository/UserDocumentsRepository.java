package com.recommender.documentsRecommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recommender.documentsRecommender.models.UserDocuments;

public interface UserDocumentsRepository extends JpaRepository<UserDocuments, String> {
	@Query("SELECT DISTINCT new UserDocuments(t.idUser, t.documentsList) FROM UserDocuments t where t.idUser = :idUser") 
    UserDocuments findUser(@Param("idUser") String idUser);
		
}
