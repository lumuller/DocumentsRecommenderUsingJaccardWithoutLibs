package com.recommender.documentsRecommender.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Entity
//@Table(name="tb_users_documents")
@Document//(collection="tb_users_documents")
public class UserDocuments {
	private static final long servialVersionUID = 1L;
		
	public UserDocuments() {
		super();
	}

	public UserDocuments(String idUser, String listDocuments) {
		super();
		this.idUser = idUser;
		this.documentsList = listDocuments;
	}

	@Id
	private String idUser;	
	
	private String documentsList;		
	
	public String getIdUser() {
		return idUser;
	}
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getDocumentsList() {
		return documentsList;
	}	
	
	public void setDocumentsList(String documentsList) {
		this.documentsList = documentsList;
	}

}
