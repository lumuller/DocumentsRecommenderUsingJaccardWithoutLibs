package com.recommender.documentsRecommender.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_users_documents")
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
