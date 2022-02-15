package com.example.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.beanMappingORdao.Greeting;
import com.example.beanMappingORdao.ResponseClass;
import com.example.beanMappingORdao.beanForBook;
import com.example.businessLogicORservice.LibraryService;
import com.example.repository.LibraryRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/library")
public class LibraryImplementation {

	private static final Logger logger=LoggerFactory.getLogger(LibraryImplementation.class);
	
	@Autowired
	LibraryRepository repo;

	@Autowired
	LibraryService service;

	@Autowired
	Greeting greeting;

	// Query Parameter
	
	@GetMapping("/greeting")
	public Greeting getBook(@RequestParam(value = "name") String nameViaURL) {
		greeting.setMsg("Hey " + nameViaURL + " welcome to our website");
		return greeting;
	}

	// Path Param
	@GetMapping("/getbook/{id}")
	public ResponseEntity getBookPathParam(@PathVariable (value="id") String id) {
		try {
		beanForBook object=service.getBook(id);//repo.findById(id).get();
		return new ResponseEntity<beanForBook>(object,HttpStatus.OK);
		}
		catch(Exception e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);		
		}
	}
	
	//Query Param
	@GetMapping("/getbooks")
	public List<beanForBook> getBooks(@RequestParam(value="authorName") String authorName){
		return repo.findAllByAuthor(authorName);
	}
	
	@GetMapping("/getAllBooks")
	public List<beanForBook> getAllBooks(){
		logger.info("get All the books");
		return repo.findAll();
	}
	
	@PutMapping("/updateAuthor/{id}")
	public ResponseEntity<beanForBook> updateBookDetailsById(@PathVariable(value="id") String id, @RequestBody beanForBook book) {
		
		beanForBook bookObj=service.getBook(id);//repo.findById(id).get();
		
		bookObj.setAisle(book.getAisle());
		bookObj.setAuthor(book.getAuthor());
		bookObj.setBook_name(book.getBook_name());
		
		repo.save(bookObj);
		
		return new ResponseEntity<beanForBook>(bookObj, HttpStatus.OK);
	}

	@PostMapping("/addBook")
	public ResponseEntity addBook(@RequestBody beanForBook book) {

		String id = service.id(book.getBook_name(), book.getAisle());//Dependency
		ResponseClass response = new ResponseClass();

		if (!service.isBookAlreadyPresent(id)) {//Dependency
			book.setId(id);
			repo.save(book);//Dependency
			HttpHeaders header = new HttpHeaders();
			header.add("unique", book.getId());
			response.setMsg("Success book is created");
			response.setId(book.getId());
			return new ResponseEntity<ResponseClass>(response, header, HttpStatus.CREATED);
		} else {
			response.setMsg("Book is present already");
			response.setId(id);
			return new ResponseEntity<ResponseClass>(response, HttpStatus.ACCEPTED);
		}

	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@DeleteMapping("/deleteBook")
	public ResponseEntity deleteBook(@RequestBody beanForBook book) {
		
		beanForBook delBook=service.getBook(book.getId());//repo.findById(book.getId()).get();
		repo.delete(delBook);
		return new ResponseEntity<>("Book is deleted", HttpStatus.CREATED);
	}
	
	

}
