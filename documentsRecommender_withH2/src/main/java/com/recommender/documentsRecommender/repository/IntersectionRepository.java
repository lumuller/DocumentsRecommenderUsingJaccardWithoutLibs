package com.recommender.documentsRecommender.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recommender.documentsRecommender.models.Intersection;

public interface IntersectionRepository extends JpaRepository<Intersection, String>{
	
	/**
	 * Return all intersections registers in which the document is presented as documentA or documentB.	
	 * @param idDocument id of the document
	 * @return a list of intersections in which the document appears
	 */
	@Query("SELECT new Intersection(t.idIntersection, t.value, t.idDocumentA, t.idDocumentB) FROM Intersection t where t.idDocumentA = :idDocument OR t.idDocumentB = :idDocument") 
    List<Intersection> findIntersectionsObjects(@Param("idDocument") String idDocument);
	
	
	/**
	 * Return the register of an intersection based on its id. In case it does not exists, return null.
	 * 
	 * PS: There are other methods (sush as getOne()) to do the same thing. However, in case the intersection does not exists, will occur an error.
	 * This method was created to avoid a previous access to database in order to verify if the register exists, before retrieve it. 
	 * 
	 * @param idIntersection id of the intersection
	 * @return a Intersection object containing the data from the intersection among two documents.
	 */
	@Query("SELECT DISTINCT new Intersection(t.idIntersection, t.value, t.idDocumentA, t.idDocumentB) FROM Intersection t where t.idIntersection = :idIntersection") 
    Intersection findIntersection(@Param("idIntersection") String idIntersection);
}
