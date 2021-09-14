package com.example.demo;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.beanMappingORdao.beanForBook;

import junit.framework.Assert;

@SpringBootTest
public class IntegrationIT {
	
	@Test
	public void findByIDintegrationTest() throws JSONException {
		
		String expected="{\r\n"
				+ "    \"book_name\": \"Cypress\",\r\n"
				+ "    \"isbn\": \"abcd\",\r\n"
				+ "    \"aisle\": 4,\r\n"
				+ "    \"author\": \"Rahul\",\r\n"
				+ "    \"id\": \"abcd4\"\r\n"
				+ "}";
		TestRestTemplate test=new TestRestTemplate();
		ResponseEntity<String> response=test.getForEntity("http://localhost:7776/library/getbook/abcd4", String.class);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		
	}
	
	@Test
	public void addBookIntegrationTest() {
		
		TestRestTemplate test=new TestRestTemplate();
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<beanForBook> request=new HttpEntity<beanForBook>(dummyBeanBookObject(),headers);
		ResponseEntity<String> response=test.postForEntity("http://localhost:7776/library/addBook", request, String.class);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		System.out.println(response.getHeaders().get("unique").get(0));
		System.out.println(response.getHeaders().get("Content-Type"));
	}
	
	public beanForBook dummyBeanBookObject() {

		beanForBook dummyBook = new beanForBook();
		dummyBook.setAisle(2);
		dummyBook.setAuthor("dummyAuthor1");
		dummyBook.setBook_name("dummyBookName1");
		dummyBook.setId("OLDdummyBookName12");
		dummyBook.setIsbn("dummyISBN1");
		return dummyBook;

	}

}
