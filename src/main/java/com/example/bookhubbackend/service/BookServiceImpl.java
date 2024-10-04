package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findBooksByCategoryTypeAndCategory(String categoryType, String category) {
        return bookRepository.findByCategoryTypeAndCategory(categoryType, category);
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findById(Long id) {
        // Alternative logic for finding a book by ID
        // This could be different from findBookById based on requirements
        return bookRepository.findById(id); // Or different implementation if needed
    }

}
