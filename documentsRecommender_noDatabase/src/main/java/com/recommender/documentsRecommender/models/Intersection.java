package com.recommender.documentsRecommender.models;

public class Intersection {
	
	private String idIntersection;
	
	private long value;
	
	private String idDocumentA;
	
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
	
	public void increaseValue() {
		value++;
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
