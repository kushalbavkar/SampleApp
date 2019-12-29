package com.home.sample.repositories;

import java.util.List;

import com.home.sample.model.Book;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long>{
    List<Book> findByTitle(String title);
}