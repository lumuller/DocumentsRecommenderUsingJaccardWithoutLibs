package com.recommender.documentsRecommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recommender.documentsRecommender.models.Intersections;
import com.recommender.documentsRecommender.models.UsersDocuments;

public interface UsersDocumentsRepository extends JpaRepository<UsersDocuments, String> {
	@Query("SELECT DISTINCT new UsersDocuments(t.idUser, t.listDocuments) FROM UsersDocuments t where t.idUser = :idUser") 
    UsersDocuments findUser(@Param("idUser") String idUser);
		
}
