package com.recommender.documentsRecommender.resources;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommender.documentsRecommender.models.Intersection;
import com.recommender.documentsRecommender.repository.IntersectionRepository;
import com.recommender.documentsRecommender.repository.UserDocumentsRepository;

@RestController
@RequestMapping(value="/www.globoplay.globo.com")
public class DocumentsUsersResource {

	UserDocumentsRepository usersDocumentsRepository;	
	IntersectionRepository intersectionsRepository;	
	private HashSet<String> documentsList;
	
	public DocumentsUsersResource() {
		usersDocumentsRepository = new UserDocumentsRepository();	
		intersectionsRepository = new IntersectionRepository();	
		documentsList = new HashSet<>();
	}
	
	
	@DeleteMapping("/")
	public void deleteDatabase() {		
		intersectionsRepository.deleteAll();
		usersDocumentsRepository.deleteAll();
		documentsList = new HashSet<>();
	}

	/**
	 * USE ONLY FOR TESTS: Runs 3k recommendation calculations.
	 *
	 */	
	/*@GetMapping("/recommendations")
	public void calculeRecommendations(){
		System.out.println("Starting time: " + new Date());		
		Random r = new Random();
		for(int i=0;i<3000;i++) {		
			int randomN = r.nextInt(1000);			
			String idDoc = "D"+ (int)(randomN);	
			resultListGeneration(idDoc);
		}		
		System.out.println("\nFinishing time: " + new Date());
	}*/
	
	/**
	 * USE ONLY FOR TESTS: Populates the database with 1 million registers, based on visualizations from
	 * 500 thousand users and 1 thousand documents.
	 */
	/*@GetMapping("/populate")
	public void populateDatabase(){
		System.out.println("Starting time: " + new Date());		
		Random r = new Random();
		for(int i=0;i<1000000;i++) {			
			String idDoc = "D"+ (int)(r.nextInt(1000));
			String idUser = "U"+ (int)(r.nextInt(500000));		
			
			System.out.printf("dur.addView(\"%s\", \"%s\")\n", idDoc, idUser);
			
			addView(idDoc, idUser);			
		}			
		System.out.println(usersDocumentsRepository.count());
		System.out.println(intersectionsRepository.count());
		System.out.println(documentsList.size());		
		System.out.println("\nFinishing time: " + new Date());		
	}*/
	
	@PostMapping("/{idDoc}/view")
	public void saveView(@RequestBody String user, @PathVariable(value="idDoc") String idDocument) {
		if(user.charAt(user.length()-1)=='=') {//not sure why, but it adds an = signal in the end of the String. The following code will remove it.
			user = user.substring(0, user.length()-1);
		}		
		addView(idDocument, user);		
	}
	
	/**
	 * Adds a new visualization into database
	 * 
	 * @param visualization a DocumentUser object built with the data sent by POST method
	 */
	public void addView(String idDocument, String idUser) {		
		
		if(usersDocumentsRepository.needToUpdateIntersections(idUser, idDocument)){
			usersDocumentsRepository.addNewVisualization(idUser, idDocument);		
			documentsList.add(idDocument);
			updateIntersectionList(idDocument, usersDocumentsRepository.getDocumentsByUser(idUser));	
		}
	}
	
	/**
	 * Updates all intersections registers of the document being visualized by the user.
	 * 
	 * @param idDocVisualized id of the document being visualized
	 * @param idUserWhoVisualized id of the user who is visualizing the document 
	 */
	private void updateIntersectionList(String idDocVisualized, HashSet<String> documentsVisualizedByTheUser){
		
		//for each document visualized by the user...		
		for(String d: documentsVisualizedByTheUser) {			
			String intersectionId = generateIdIntersection(idDocVisualized, d);		
			
			//retrieves the intersection from database 
			Intersection intersection = intersectionsRepository.getIntersection(intersectionId);			
			
			//if the intersection register between two documents already exists...
			if(intersection!=null){
				//just add 1 to the value of the intersection
				intersection.increaseValue();			
			} else {		
				//else, creates a new one, with initial value equals to 1.
				intersection = new Intersection(intersectionId, 1, idDocVisualized, d);			
				//Send the list to be saved into database
				intersectionsRepository.addNewIntersection(intersection);
			}	
		}		
	}
	
	/**
	 * Generates an intersection id to be used as reference in the register of the intersection among doc1 and doc2. 
	 * 
	 * @param doc1 id of the first document
	 * @param doc2 id of the second document
	 * @return a String value with the id which represents the intersection register from these documents.
	 */
	public String generateIdIntersection(String doc1, String doc2) {
		String identifier;
		
		//the id is based on the result of lexicographically comparing
		if(doc1.compareToIgnoreCase(doc2) < 0) { 
			identifier = doc1 + doc2;
		} else {
			identifier = doc2 + doc1;
		} 		
		return identifier;
	}
	
	@GetMapping("/{id}/similarity")
	public List<ResultPresentation> getsDocumentSimilarities(@PathVariable(value="id") String idDocument){
		return resultListGeneration(idDocument);
	}
	
	/**
	 * Generates the result list with the identification and the similarity score from the
	 * 10 documents most similar to baseDocument. 
	 * 
	 * The similarity is calculated by using the Jaccard similarity coefficient.    
	 * 
	 * @param baseDocument the document used as base to the recommendation
	 * @return an list with the similar documents. Each item of the list contains the id of the similar document and also the similarity score.
	 */
	 public List<ResultPresentation> resultListGeneration(String baseDocument){	    	
	    	List<ResultPresentation> resultList = new ArrayList<ResultPresentation>(); 	    	
	    	//for each document already register in the system...
	    	for(String k : documentsList) {
	    		if(k.equals(baseDocument)) {//do not compare the similarity of the document with itself
	    			continue;
	    		} else {	    			
	    			Long sizeBase=null, sizeCompared=null, sizeIntersection=null;
	    			String idIntersection;
	    			
	    			if(k.compareToIgnoreCase(baseDocument) < 0) {
	    				idIntersection = k + baseDocument;
	    			} else {
	    				idIntersection = baseDocument + k;
	    			}
	    			
	    			if(intersectionsRepository.getIntersection(idIntersection) == null) {//if does not exists an intersection among the documents...
	    				continue;//then, there is no similarity to be checked.
	    			} else {
	    				//retrieves the necessary data to calculates the coefficient
	    				sizeCompared = intersectionsRepository.getIntersection(k + k).getValue();
	    				sizeBase = intersectionsRepository.getIntersection(baseDocument + baseDocument).getValue();
	    				sizeIntersection = intersectionsRepository.getIntersection(idIntersection).getValue();
 					
	 					double jaccardIndex = sizeIntersection / ((double) (sizeBase + sizeCompared) - sizeIntersection);
	 					resultList.add(new ResultPresentation(k, jaccardIndex));
	    			}
	    		}
	    		
	    	}	    		
	    		
	    	resultList.sort(Comparator.comparingDouble(ResultPresentation::getScore).reversed()); //sort the list from biggest to smallest score value	
	    	//reduce the list to the 10 first occurrences. If the list has less than 10 items, them, the method will retrieve all.
	    	if(resultList.size() > 9) {
	    		resultList = resultList.subList(0, 9);
	    	} else {
	    		resultList = resultList.subList(0, resultList.size());
	    	}	
	    	return resultList;	    	
	    }
	 
		/**
		 * 
		 * Inner class to represent the results presentation format.		 
		 */
	 	private class ResultPresentation{		 
			private String url;
			private double score;	
			
			public ResultPresentation(String url, double score) {				
				this.url = "www.globoplay.globo.com/v/" + url;
				this.score = score;
			}
					
			public String getUrl() {
				return url;
			}

			public double getScore() {
				return score;
			}
	 }	
}
