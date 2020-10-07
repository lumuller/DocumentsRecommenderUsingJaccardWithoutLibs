package com.recommender.documentsRecommender.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommender.documentsRecommender.models.Intersection;
import com.recommender.documentsRecommender.models.UserDocuments;
import com.recommender.documentsRecommender.repository.IntersectionRepository;
import com.recommender.documentsRecommender.repository.UserDocumentsRepository;

@RestController
@RequestMapping(value="/www.recommender.com")
public class DocumentsUsersResource {
		
	@Autowired
	UserDocumentsRepository usersDocumentsRepository;
	
	@Autowired
	IntersectionRepository intersectionsRepository;
	
	@DeleteMapping("/deleteAll")
	public void deleteDatabase() {		
		intersectionsRepository.deleteAll();
		usersDocumentsRepository.deleteAll();
	}
	
	/**
	 * USE ONLY FOR TESTS: Runs 3k recommendation calculations.
	 * @return A String with the starting hour and the finishing hour.
	 */	
	@GetMapping("/recommendations")
	public String calculeRecommendations(){
		String metrics = "";
		metrics = metrics.concat("Starting time: " + new Date());		
		Random r = new Random();
		for(int i=0;i<3000;i++) {		
			int randomN = r.nextInt(100);
			System.out.println("R: " + i);
			String idDoc = "D"+ (int)(randomN);	
			resultListGeneration(idDoc);
		}		
		metrics = metrics.concat("\nFinishing time: " + new Date());
		return metrics;
	}
	
	/**
	 * USE ONLY FOR TESTS: Populates the database.
	 * @return A String with the starting hour and the finishing hour.
	 */
	@GetMapping("/populate")
	public void populateDatabase(){
		System.out.println("Starting time: " + new Date());
		
		Random r = new Random();
		System.out.println("Inserindo 100 mil registros...");
		for(int i=0;i<10000;i++) {			
			String idDoc = "D"+ (int)(r.nextInt(100));
			String idUser = "U"+ (int)(r.nextInt(5000));			
			addView(idDoc, idUser);			
		}	
		System.out.println("\nFinishing time: " + new Date());		
	}
	
	@PostMapping("/v/{idDoc}/view")
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
		//retrieves the user register from database
		UserDocuments ud = null;
		Optional<UserDocuments> udOptional = usersDocumentsRepository.findById(idUser);
		//if is the first visualization performed by the user, he will not exists into database...
		if(!udOptional.isPresent()) {
			//then, create a new user, include the document in the list of documents he visualized.
			ud = new UserDocuments();
			ud.setIdUser(idUser);
			ud.setDocumentsList(idDocument+",");			
			//save the new register into database
			usersDocumentsRepository.save(ud);	
			//update the intersections registers from this document, based on the documents previously visualized by the user 
			//(Ps: even in this case, when the user have never visualized anything, we use this method to update the value of the document self-intersection
			updateIntersectionList(idDocument, ud);
		} else {
			ud = udOptional.get();			
			String [] documentsList = ud.getDocumentsList().split(",");
			//else, includes the new document in users list, only if the user have never visualized it before
			if(!Arrays.asList(documentsList).contains(idDocument)) {
				ud.setDocumentsList(ud.getDocumentsList()+idDocument+",");
				//save the new register into database
				usersDocumentsRepository.save(ud);	
				//update the intersections registers from this document, based on the documents previously visualized by the user
				updateIntersectionList(idDocument, ud);
			}
		}
	}
	
	/**
	 * Updates all intersections registers of the document being visualized by the user.
	 * 
	 * @param idDocVisualized id of the document being visualized
	 * @param idUserWhoVisualized id of the user who is visualizing the document 
	 */
	private void updateIntersectionList(String idDocVisualized, UserDocuments userWhoVisualized){
		//split the list of all the documents visualized by the user. We need to access the document ids individually
		String [] documentsVisualizedByTheUser =  userWhoVisualized.getDocumentsList().split(",");		
		
		//This list will keep all the changes made on the intersections, to perform an save all (faster than save one by one).
		List<Intersection> updatedIntersections = new ArrayList<>();	
		
		//for each document visualized by the user...		
		for(String d: documentsVisualizedByTheUser) {
			//We generated it correspond intersection id (based on the id of the document recently visualized, and the id of the document visualized in the past)
			//The intersection represents the number of users who co-visualized two documents
			
			//When we invoke this method, the document recently visualized is already in the list of documents visualized by the user.
			//The intersection of the document with itself correspond to the amount of users who visualized it. 
			//We call this special intersection as self-intersection ID.			
			String intersectionId = generateIdIntersection(idDocVisualized, d);		
			
			//retrieves the intersection from database 
			Optional<Intersection> intersectionOpt = intersectionsRepository.findById(intersectionId);			
			Intersection intersection = null;
			//if the intersection register between two documents already exists...
			if(intersectionOpt.isPresent()){
				intersection = intersectionOpt.get();
				//just add 1 to the value of the intersection
				long newSize = intersection.getValue() + 1;
				intersection.setValue(newSize);			
			} else {		
				//else, creates a new one, with initial value equals to 1.
				intersection = new Intersection(intersectionId, 1, idDocVisualized, d);			
			}		
			//add the intersections (new one or a modified one) to the list.
			updatedIntersections.add(intersection);			
			
		}		
		//Send the list to be saved into database
		intersectionsRepository.saveAll(updatedIntersections);
		
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
		//Creates a new list to storage the results.	
		List<ResultPresentation> resultList = new ArrayList<ResultPresentation>(); 	    	
	    	
			//Retrieving the list of intersection registers from this document
	    	List<Intersection> intersectionsObjectsA = intersectionsRepository.findByIdDocumentA(baseDocument);
	    	List<Intersection> intersectionsObjectsB = intersectionsRepository.findByIdDocumentB(baseDocument);
	    	
	    	List<Intersection> intersectionsObjects = new ArrayList<>();
	    	intersectionsObjects.addAll(intersectionsObjectsA);
	    	intersectionsObjects.addAll(intersectionsObjectsB);
	    	
	    	//retrieves the self-intersection register from the base document
	    	Intersection baseDocumentSelfIntersectionRegister = intersectionsObjects.stream()
					  .filter(intersection -> (baseDocument+baseDocument).equals(intersection.getIdIntersection()))
					  .findAny()
					  .orElse(null);
	    	
	    	//for each one of this registers
	    	for(Intersection k : intersectionsObjects) {	    	
	    		Long sizeBase=null, sizeCompared=null, sizeIntersection=null;
	    		if(k.getIdIntersection().equalsIgnoreCase(baseDocument + baseDocument)) {
	    			//if we are looking to a self-intersection register, just ignore. Otherwise, we will calculate the similarity between the document itself.	    			
	    			continue;
	    		} else {	    
	    			//retrieves the self-intersection register from the document being compared	    			
	    			Optional<Intersection> comparedDocumentSelfIntersectionRegisterOpt = 
	    					baseDocument.equals(k.getIdDocumentA()) ? 
							intersectionsRepository.findById(k.getIdDocumentB() + k.getIdDocumentB()) : 
							intersectionsRepository.findById(k.getIdDocumentA() + k.getIdDocumentA());	    			
	    			
					Intersection comparedDocumentSelfIntersectionRegister = comparedDocumentSelfIntersectionRegisterOpt.get();
											
					//determine the number needed to calculates the jaccard index.		
    				sizeCompared = comparedDocumentSelfIntersectionRegister.getValue(); //number of user who visualized the comparedDocument			
    				sizeIntersection = k.getValue(); //number of users who co-visualized document comparedDocument and baseDocument					
    				sizeBase = baseDocumentSelfIntersectionRegister.getValue(); //number of users who visualized document baseDocument
    				
    				double jaccardIndex = sizeIntersection / ((double) (sizeBase + sizeCompared) - sizeIntersection); //Jaccard coefficient based on intersection divided by union.    				
    				
    				resultList.add(new ResultPresentation(
    						comparedDocumentSelfIntersectionRegister.getIdDocumentA(), jaccardIndex));
	    		}
	    			    		
	    	}	    		
	    	
	    	if(resultList.size() > 0) {
		    	resultList.sort(Comparator.comparingDouble(ResultPresentation::getScore).reversed()); //sort the list from biggest to smallest score value	    	
		    	
		    	//reduce the list to the 10 first occurrences. If the list has less than 10 items, them, the method will retrieve all.
		    	if(resultList.size() > 9) {
		    		resultList = resultList.subList(0, 9);
		    	} else {
		    		resultList = resultList.subList(0, resultList.size());
		    	}  
	    	} else {
	    		resultList.add(new ResultPresentation("Non-existent document or non-existent similar URLs", 0));
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
				this.url = url;
				this.score = score;
			}			
			public String getUrl() {
				return "www.recommender.com/v/" + url;
			}
			public double getScore() {
				return score;
			}
	 }	
}
