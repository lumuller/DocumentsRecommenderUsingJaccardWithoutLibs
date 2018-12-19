package com.recommender.documentsRecommender.models;


import java.util.HashSet;

public class UserDocuments {
			
	public UserDocuments() {
		super();
	}

	public UserDocuments(String idUser, String firstDocument) {
		super();
		this.idUser = idUser;
		this.documentsList = new HashSet<>();
		this.documentsList.add(firstDocument);
	}

	private String idUser;	
	
	private HashSet<String> documentsList;		
	
	public void addDocument(String idDocument) {
		documentsList.add(idDocument);
	}
		
	public String getIdUser() {
		return idUser;
	}
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public HashSet<String> getDocumentsList() {
		return documentsList;
	}

	public void setDocumentsList(HashSet<String> documentsList) {
		this.documentsList = documentsList;
	}

	

}
