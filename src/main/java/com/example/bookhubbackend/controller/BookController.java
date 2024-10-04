package com.example.bookhubbackend.controller;

import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 카테고리 타입과 카테고리에 해당하는 책 목록을 조회
    @GetMapping("/category")
    public List<Book> getBooksByCategoryTypeAndCategory(
            @RequestParam String categoryType,
            @RequestParam String category) {
        return bookService.findBooksByCategoryTypeAndCategory(categoryType, category);
    }

    // 책 상세 조회
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }
}
