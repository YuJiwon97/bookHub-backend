package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findBooksByCategoryTypeAndCategory(String categoryType, String category) {
        return bookRepository.findByCategoryTypeAndCategory(categoryType, category);
    }
}
