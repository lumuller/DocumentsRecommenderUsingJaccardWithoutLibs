package com.recommender.documentsRecommender.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_users_documents")
public class UsersDocuments {
	private static final long servialVersionUID = 1L;
		
	public UsersDocuments() {
		super();
	}

	public UsersDocuments(String idUser, String listDocuments) {
		super();
		this.idUser = idUser;
		this.listDocuments = listDocuments;
	}

	@Id
	private String idUser;	
	
	private String listDocuments;		
	
	public String getIdUser() {
		return idUser;
	}
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getListDocuments() {
		return listDocuments;
	}	
	
	public void setListDocuments(String listDocuments) {
		this.listDocuments = listDocuments;
	}

}
