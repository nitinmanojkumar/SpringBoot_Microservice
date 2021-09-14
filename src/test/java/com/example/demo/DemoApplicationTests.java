package com.example.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.example.beanMappingORdao.ResponseClass;
import com.example.beanMappingORdao.beanForBook;
import com.example.businessLogicORservice.LibraryService;
import com.example.repository.LibraryRepository;
import com.example.resource.LibraryImplementation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	private SoftAssertions softassert;

	@Autowired
	private MockMvc mockmvc;

	@Autowired
	LibraryImplementation controller;

	@MockBean
	LibraryRepository repo;

	@MockBean
	LibraryService service;

	@BeforeEach
	public void setUp() {
		softassert = new SoftAssertions();
	}

	@Test // Verifying the service/logic method that creates the ID, not testing the HTTP
			// methods such as put, post etc
	public void checkCreatingID() {

		LibraryService service = new LibraryService();
		String id = service.id("Z", 1);
		softassert.assertThat(id).isEqualTo("NEWZ1");
		id = service.id("Y", 1);
		softassert.assertThat(id).isEqualTo("OLDY1");
		softassert.assertAll();

	}
	
	//TODO Service or Business layer methods as part of Unit Testing
	@Test// Service method unit testing
	public void checkIsBookAlreadyPresentMethod() {
		
	}

	// To test the method either the logic or controller method
	@Test // POST//Verifying the HTTP method implementation or controller by calling the
			// methods directly
	public void checkAddBookByMethod() {

		beanForBook book = dummyBeanBookObject();
		ResponseClass response = new ResponseClass();
		// Mock the dependent methods
		when(service.id(book.getBook_name(), book.getAisle())).thenReturn(book.getId());
		when(service.isBookAlreadyPresent(book.getId())).thenReturn(false);
		when(repo.save(any())).thenReturn(book);
		// Call the
		ResponseEntity<?> entity = controller.addBook(dummyBeanBookObject());
		response = (ResponseClass) entity.getBody();
		softassert.assertThat(entity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
		softassert.assertThat(response.getMsg()).isEqualTo("Success book is created");
		softassert.assertThat(response.getId()).isEqualTo(book.getId());
		// assertEquals(entity.getStatusCode(), HttpStatus.CREATED);
		// Mock the dependent methods
		when(service.id(book.getBook_name(), book.getAisle())).thenReturn(book.getId());
		when(service.isBookAlreadyPresent(book.getId())).thenReturn(true);
		when(repo.save(any())).thenReturn(book);
		entity = controller.addBook(dummyBeanBookObject());
		response = (ResponseClass) entity.getBody();
		softassert.assertThat(entity.getStatusCode()).isEqualByComparingTo(HttpStatus.ACCEPTED);
		softassert.assertThat(response.getId()).isEqualTo(book.getId());
		softassert.assertThat(response.getMsg()).isEqualTo("Book is present already");
		// assertEquals(response.getId(), book.getId());
		softassert.assertAll();
	}

	// To test the controller making a API call to the endpoint without having to
	// start the server
	@Test // POST//Verifying the HTTP method implementation or controller by calling the
			// endpoint
	public void checkAddBookByEndPoint() throws Exception {

		beanForBook book = dummyBeanBookObject();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(book);
		// Mock the dependent methods
		when(service.id(book.getBook_name(), book.getAisle())).thenReturn(book.getId());
		when(service.isBookAlreadyPresent(book.getId())).thenReturn(false);
		when(repo.save(any())).thenReturn(book);
		// Mock the service call without starting the server
		this.mockmvc.perform(post("/library/addBook").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(book.getId()))
				.andExpect(jsonPath("$.msg").value("Success book is created"));

		// Mock the dependent methods
		when(service.id(book.getBook_name(), book.getAisle())).thenReturn(book.getId());
		when(service.isBookAlreadyPresent(book.getId())).thenReturn(true);
		when(repo.save(any())).thenReturn(book);
		// Mock the service call without starting the server
		this.mockmvc.perform(post("/library/addBook").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andDo(print()).andExpect(status().isAccepted()).andExpect(jsonPath("$.id").value(book.getId()))
				.andExpect(jsonPath("$.msg").value("Book is present already"));

	}

	@Test
	public void checkFindBookbyAuthor() throws Exception {
		beanForBook book = dummyBeanBookObject();
		List<beanForBook> list = new ArrayList<beanForBook>();
		list.add(book);
		list.add(book);
		when(repo.findAllByAuthor("")).thenReturn(list);

		this.mockmvc.perform(get("/library/getbooks").param("authorName", "")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(2))).andExpect(jsonPath("$.[0].id").value("dummyID"));
	}

	@Test // PUT
	public void checkUpdateBook() throws Exception {
		beanForBook book=dummyBeanBookObject();
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writeValueAsString(updateBeanBookObject());
		when(service.getBook(any())).thenReturn(dummyBeanBookObject());
		
		this.mockmvc.perform(put("/library/updateAuthor/"+book.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(content().json("{\"book_name\":\"updatedBookName\",\"isbn\":\"dummyISBN\",\"aisle\":2,\"author\":\"updatedAuthor\",\"id\":\"dummyID\"}"));
		
		
	}

	@Test // DELETE
	public void checkDeleteBook() throws Exception {
		beanForBook book=dummyBeanBookObject();
		ObjectMapper mapper=new ObjectMapper();
		String jsonString=mapper.writeValueAsString(book);
		
		when(service.getBook(any())).thenReturn(book);
		
		this.mockmvc.perform(delete("/library/deleteBook").contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"The Secret II9785\"}"))
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(content().string("Book is deleted"));
	}

	public beanForBook dummyBeanBookObject() {

		beanForBook dummyBook = new beanForBook();
		dummyBook.setAisle(1);
		dummyBook.setAuthor("dummyAuthor");
		dummyBook.setBook_name("dummyBookName");
		dummyBook.setId("dummyID");
		dummyBook.setIsbn("dummyISBN");
		return dummyBook;

	}

	public beanForBook updateBeanBookObject() {

		beanForBook dummyBook = new beanForBook();
		dummyBook.setAisle(2);
		dummyBook.setAuthor("updatedAuthor");
		dummyBook.setBook_name("updatedBookName");
		dummyBook.setId("updatedID");
		dummyBook.setIsbn("updatedISBN");
		return dummyBook;

	}

}
