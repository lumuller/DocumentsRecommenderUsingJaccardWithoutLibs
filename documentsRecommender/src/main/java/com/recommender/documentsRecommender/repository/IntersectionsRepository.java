package com.recommender.documentsRecommender.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recommender.documentsRecommender.models.Intersections;

public interface IntersectionsRepository extends JpaRepository<Intersections, String>{
	
	/**
	 * Return all intersections registers in which the document is presented as documentA or documentB.	
	 * @param idDocument id of the document
	 * @return a list of intersections in which the document appears
	 */
	@Query("SELECT new Intersections(t.idIntersection, t.value, t.idDocumentA, t.idDocumentB) FROM Intersections t where t.idDocumentA = :idDocument OR t.idDocumentB = :idDocument") 
    List<Intersections> findIntersectionsObjects(@Param("idDocument") String idDocument);
	
	@Query("SELECT DISTINCT new Intersections(t.idIntersection, t.value, t.idDocumentA, t.idDocumentB) FROM Intersections t where t.idIntersection = :idIntersection") 
    Intersections findIntersection(@Param("idIntersection") String idIntersection);
}
