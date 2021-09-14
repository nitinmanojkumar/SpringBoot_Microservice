package com.example.repository;

import java.util.List;

import com.example.beanMappingORdao.beanForBook;

public interface LibraryRepositoryCustom {

	List<beanForBook> findAllByAuthor(String authorName);
	
}
