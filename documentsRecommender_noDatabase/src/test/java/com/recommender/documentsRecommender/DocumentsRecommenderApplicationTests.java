package com.recommender.documentsRecommender;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.google.gson.Gson;
import com.recommender.documentsRecommender.resources.DocumentsUsersResource;

@SpringBootTest
public class DocumentsRecommenderApplicationTests {
		
	
	
	/**
	 * Verifies a scenario in which 3 documents were co-visualized by the same
	 * users. When the similarity of any of them is required, the other 2 documents
	 * must be recommended with a similarity score equals to 1.
	 */
	@Test
	public void simpleSimilarityTest() {  		   		   		   
	
		DocumentsUsersResource dur = new DocumentsUsersResource();
		
		dur.addView("D1", "U1");
		dur.addView("D1", "U2");
		dur.addView("D2", "U1");
		dur.addView("D2", "U2");
		dur.addView("D3", "U1");
		dur.addView("D3", "U2");
		
		String similarityD1 = new Gson().toJson(dur.getsDocumentSimilarities("D1"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D2\",\"score\":1.0},"
				+ "{\"url\":\"www.globoplay.globo.com/v/D3\",\"score\":1.0}]", similarityD1);
		
		String similarityD2 = new Gson().toJson(dur.getsDocumentSimilarities("D2"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D1\",\"score\":1.0},"
				+ "{\"url\":\"www.globoplay.globo.com/v/D3\",\"score\":1.0}]", similarityD2);
		
		String similarityD3 = new Gson().toJson(dur.getsDocumentSimilarities("D3"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D1\",\"score\":1.0},"
				+ "{\"url\":\"www.globoplay.globo.com/v/D2\",\"score\":1.0}]", similarityD3);
    }	
	
	@Test
	public void similarityTest() {
		DocumentsUsersResource dur = new DocumentsUsersResource();
		/*
		 * Visualization by users:
		 * U0: D1
		 * U2: D0, D1, D3
		 * U3: D1
		 * U5: D0, D3, D4
		 * U6: D0, D3
		 * U7: D1, D4
		 * U8: D3
		 * U9: D2, D4
		 * 
		 */
		dur.addView("D1", "U2"); 
		dur.addView("D2", "U9");
		dur.addView("D3", "U8");
		dur.addView("D3", "U5");
		dur.addView("D1", "U0");
		dur.addView("D1", "U3");
		dur.addView("D0", "U5");
		dur.addView("D4", "U9");
		dur.addView("D0", "U5");
		dur.addView("D0", "U2");
		dur.addView("D3", "U2");
		dur.addView("D4", "U9");
		dur.addView("D3", "U8");
		dur.addView("D1", "U3");
		dur.addView("D3", "U5");
		dur.addView("D1", "U7");
		dur.addView("D3", "U6");
		dur.addView("D4", "U7");
		dur.addView("D0", "U6");
		dur.addView("D4", "U5");
		
		String similarityD0 = new Gson().toJson(dur.getsDocumentSimilarities("D0"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D3\",\"score\":0.75},{\"url\":\"www.globoplay.globo.com/v/D4\",\"score\":0.2},{\"url\":\"www.globoplay.globo.com/v/D1\",\"score\":0.16666666666666666}]", similarityD0);
		
		String similarityD1 = new Gson().toJson(dur.getsDocumentSimilarities("D1"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D4\",\"score\":0.16666666666666666},{\"url\":\"www.globoplay.globo.com/v/D0\",\"score\":0.16666666666666666},{\"url\":\"www.globoplay.globo.com/v/D3\",\"score\":0.14285714285714285}]", similarityD1);
		
		String similarityD2 = new Gson().toJson(dur.getsDocumentSimilarities("D2"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D4\",\"score\":0.3333333333333333}]", similarityD2);
		
		String similarityD3 = new Gson().toJson(dur.getsDocumentSimilarities("D3"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D0\",\"score\":0.75},{\"url\":\"www.globoplay.globo.com/v/D4\",\"score\":0.16666666666666666},{\"url\":\"www.globoplay.globo.com/v/D1\",\"score\":0.14285714285714285}]", similarityD3);
		
		String similarityD4 = new Gson().toJson(dur.getsDocumentSimilarities("D4"));	
		assertEquals("[{\"url\":\"www.globoplay.globo.com/v/D2\",\"score\":0.3333333333333333},{\"url\":\"www.globoplay.globo.com/v/D0\",\"score\":0.2},{\"url\":\"www.globoplay.globo.com/v/D1\",\"score\":0.16666666666666666},{\"url\":\"www.globoplay.globo.com/v/D3\",\"score\":0.16666666666666666}]", similarityD4);		
	}
}
