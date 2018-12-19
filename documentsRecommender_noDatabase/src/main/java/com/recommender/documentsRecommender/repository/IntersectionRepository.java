package com.recommender.documentsRecommender.repository;

import java.util.HashMap;
import java.util.Set;

import com.recommender.documentsRecommender.models.Intersection;

public class IntersectionRepository {
	
	private HashMap<String, Intersection> intersections;
	
	public IntersectionRepository() {
		intersections = new HashMap<>();
	}
	
	
	/**
	 * Adds a new Intersection register to the HashMap
	 * @param i Intersection object.
	 */
	public void addNewIntersection(Intersection i) {
		if(!intersections.containsKey(i.getIdIntersection())){
			intersections.put(i.getIdIntersection(), i);            
        }		
	}
	
	/**
	 * Return the Intersection object based on an intersection id
	 * @param idIntersection the id of an intersection
	 * @return the Intersection  object containing the data of the intersection among two documents.
	 */
	public Intersection getIntersection(String idIntersection){
		if(intersections.containsKey(idIntersection)) {
			return intersections.get(idIntersection);
		} else {
			return null;
		}
	}
		
	public Set<String> getKeys(){
		return intersections.keySet();
	}
	
	public int count() {
		return intersections.size();
	}
	
	public void deleteAll(){
		intersections = new HashMap<>();
	}
	
}
