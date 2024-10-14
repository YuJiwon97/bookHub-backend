package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findBooksByCategoryTypeAndCategory(String categoryType, String category);
    Optional<Book> findBookById(Long id);
    Optional<Book> findById(Long id);
    String findBookTitleById(Long id);
}
