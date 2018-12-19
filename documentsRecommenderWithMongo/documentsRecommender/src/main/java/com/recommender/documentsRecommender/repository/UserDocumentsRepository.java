package com.recommender.documentsRecommender.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recommender.documentsRecommender.models.UserDocuments;

public interface UserDocumentsRepository extends MongoRepository<UserDocuments, String> {
	
	/*
	@Query("SELECT DISTINCT new UserDocuments(t.idUser, t.documentsList) FROM UserDocuments t where t.idUser = :idUser") 
    UserDocuments findUser(@Param("idUser") String idUser);
    
    */	
}
