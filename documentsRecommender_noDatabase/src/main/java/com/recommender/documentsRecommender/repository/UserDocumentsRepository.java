package com.recommender.documentsRecommender.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.recommender.documentsRecommender.models.UserDocuments;

public class UserDocumentsRepository {
	
	private HashMap<String, UserDocuments> documentsVisualizedByTheUser;
	
	public UserDocumentsRepository() {
		documentsVisualizedByTheUser = new HashMap<>(); 
	}
	
	/**
	 * Add a new documents to the user visualization list.
	 * 
	 * @param idUser id of the user who visualized the document
	 * @param idDocument id of the visualized document 
	 */
	public void addNewVisualization(String idUser, String idDocument) {
		if(!documentsVisualizedByTheUser.containsKey(idUser)){
			documentsVisualizedByTheUser.put(idUser, new UserDocuments(idUser, idDocument));            
        } else {
        	documentsVisualizedByTheUser.get(idUser).addDocument(idDocument); 
        }
		
	}
	
	/**
	 * Returns the list of documents visualized by the user
	 * 
	 * @param idUser id of the the user
	 * @return a HashSet containing the ids from all documents visualized by the user
	 */
	public HashSet<String> getDocumentsByUser(String idUser){
		if(documentsVisualizedByTheUser.containsKey(idUser)) {
			return documentsVisualizedByTheUser.get(idUser).getDocumentsList();
		} else {
			return null;
		}
	}
	
	/**
	 * Checks if is necessary to update the intersections from an document. If the document was already visualized by the user, 
	 * then, that is no need to update the intersections.
	 * 
	 * @param idUser Id of the user who is visualizing the document
	 * @param idDocument Id of the document visualized by the user
	 * 
	 * @return a boolean value representing if is necessary to update all the intersections register in which the document appears
	 */
	public boolean needToUpdateIntersections(String idUser, String idDocument) {
		if(documentsVisualizedByTheUser.containsKey(idUser)) {
			if(documentsVisualizedByTheUser.get(idUser).getDocumentsList().contains(idDocument)) {
				return false;
			} else {
				return true;
			}			
		} else {
			return true;
		}
		
	}
	
	public Set<String> getKeys(){
		return documentsVisualizedByTheUser.keySet();
	}
	
	public int count() {
		return documentsVisualizedByTheUser.size();
	}
	
	public void printAll() {
		Set<String> keys = documentsVisualizedByTheUser.keySet();    	
    	for(String k : keys) {		
			System.out.println(k + ": " + documentsVisualizedByTheUser.get(k).toString());
		}
	}
	
	public void deleteAll() {
		documentsVisualizedByTheUser = new HashMap<>(); 
	}
		
}
