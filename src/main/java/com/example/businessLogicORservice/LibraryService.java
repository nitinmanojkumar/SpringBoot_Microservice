package com.example.businessLogicORservice;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.example.beanMappingORdao.beanForBook;
import com.example.repository.LibraryRepository;

@Service
public class LibraryService {

	@Autowired
	LibraryRepository repo;
	
	//public static String id(String bookNM,int aisle) {
	public String id(String bookNM,int aisle) {
		if(bookNM.startsWith("Z")) {
			return "NEW"+bookNM+aisle;
		}
		return "OLD"+bookNM+aisle;
	}
	
	public boolean isBookAlreadyPresent(String id) {
		Optional<beanForBook> lib=repo.findById(id);
		if(lib.isPresent()) 
			return true;
		else
			return false;
	}
	
	public beanForBook getBook(String id) {
		return repo.findById(id).get();
	}
	
	
}
