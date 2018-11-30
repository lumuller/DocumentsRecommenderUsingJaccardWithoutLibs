package com.recommender.documentsRecommender;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class DocumentsRecommenderApplicationTests extends AbstractTest{

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
		
	@Test
	public void getProductsList() throws Exception {  		   		   		   
		   
		String uri = "/populate";	 
		
		
		   
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
	      
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	      
	   }
	
	 /*  @Test
	   public void createView() throws Exception {
	      String uri = "http://localhost:8080/www.globoplay.globo.br/v/D1/view";	      
	      
	      JSONObject obj = new JSONObject();
	        obj.put("user", "U1"); 
	        
	      String input = obj.toString();
	      
	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
	 	         .contentType(MediaType.ALL_VALUE)
	 	         .content(input)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(201, status);
	      String content = mvcResult.getResponse().getContentAsString();
	      
	      assertEquals(content, "View was recorded successfully");     
	      
	      
	      //assertEquals(content, "Post is created successfully");*/
	      
	      /*Product product = new Product();
	      product.setId("3");
	      product.setName("Ginger");
	      String inputJson = super.mapToJson(product);
	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
	         .contentType(MediaType.APPLICATION_JSON_VALUE)
	         .content(inputJson)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(201, status);
	      String content = mvcResult.getResponse().getContentAsString();
	      assertEquals(content, "Product is created successfully");*/
	   //}
}
