package com.recommender.documentsRecommender.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//@Entity
//@Table(name="tb_intersections")
@Document//(collection="tb_intersections")
public class Intersection {
	private static final long servialVersionUID = 1L;
	
	@Id
	private String idIntersection;
	
	private long value;
	@Indexed
	private String idDocumentA;
	@Indexed
	private String idDocumentB;	
	
	public Intersection() {
		super();
	}

	public Intersection(String idIntersection, long value, String idDocumentA, String idDocumentB) {
		super();
		this.idIntersection = idIntersection;
		this.value = value;
		this.idDocumentA = idDocumentA;
		this.idDocumentB = idDocumentB;
	}

	public String getIdDocumentA() {
		return idDocumentA;
	}

	public void setIdDocumentA(String idDocumentA) {
		this.idDocumentA = idDocumentA;
	}

	public String getIdDocumentB() {
		return idDocumentB;
	}

	public void setIdDocumentB(String idDocumentB) {
		this.idDocumentB = idDocumentB;
	}

	public String getIdIntersection() {
		return idIntersection;
	}

	public void setIdIntersection(String idIntersection) {
		this.idIntersection = idIntersection;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}
