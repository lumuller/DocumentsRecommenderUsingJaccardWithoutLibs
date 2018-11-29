package com.recommender.documentsRecommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommender.documentsRecommender.models.UsersDocuments;

public interface UsersDocumentsRepository extends JpaRepository<UsersDocuments, String> {
		
		
}
