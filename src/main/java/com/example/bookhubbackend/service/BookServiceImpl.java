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
        return bookRepository.findById(id);
    }

    @Override
    public String findBookTitleById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            String title = optionalBook.get().getTitle();
            System.out.println("책 ID: " + id + " | 책 제목: " + title);
            return title;
        } else {
            System.out.println("책 ID: " + id + " | 책을 찾을 수 없습니다.");
            return "책 제목이 없습니다.";
        }
    }
}
