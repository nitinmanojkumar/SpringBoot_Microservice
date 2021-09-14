package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.beanMappingORdao.beanForBook;

//@Repository
public class LibraryRepostoryCustomImpl implements LibraryRepositoryCustom{

	@Autowired
	LibraryRepository repo;
	
	@Override
	public List<beanForBook> findAllByAuthor(String authorName) {
		List<beanForBook> allItems=repo.findAll();
		List<beanForBook> newItems=new ArrayList();
		for(beanForBook item : allItems) {
			//if(item.getAuthor().equalsIgnoreCase("Its")) {
				newItems.add(item);
			//}
		}
		return allItems;
	}
}
