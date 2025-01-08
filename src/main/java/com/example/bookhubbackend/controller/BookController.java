package com.example.bookhubbackend.controller;

import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 카테고리별 도서 조회
    @GetMapping("/category")
    public ResponseEntity<List<Book>> getBooksByCategory(
            @RequestParam(required = false, defaultValue = "") String categoryType,
            @RequestParam(required = false, defaultValue = "") String category) {
        try {
            if (categoryType.isEmpty() || category.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // 요청 파라미터가 없을 경우 400 반환
            }
            List<Book> books = bookService.getBooksByCategory(categoryType, category);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    // 책 상세 조회
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    // 모든 책 목록 조회
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAllBooks();
    }

    // 책 추가
    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    // 책 삭제
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
