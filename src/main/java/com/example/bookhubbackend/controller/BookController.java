package com.example.bookhubbackend.controller;

import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
