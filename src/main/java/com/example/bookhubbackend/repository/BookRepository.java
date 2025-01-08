package com.example.bookhubbackend.repository;

import com.example.bookhubbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryTypeAndCategory(String categoryType, String category);
//    List<Book> findBooksByCategory(String categoryType, String category);
}
