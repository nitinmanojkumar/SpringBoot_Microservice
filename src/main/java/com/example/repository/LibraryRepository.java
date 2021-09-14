package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.beanMappingORdao.beanForBook;

@Repository
public interface LibraryRepository extends JpaRepository<beanForBook, String>,LibraryRepositoryCustom {

}
