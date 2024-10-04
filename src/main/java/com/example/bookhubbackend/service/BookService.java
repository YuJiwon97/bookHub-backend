package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    // 카테고리 타입과 카테고리별 책 목록 조회
    List<Book> findBooksByCategoryTypeAndCategory(String categoryType, String category);
    Optional<Book> findBookById(Long id);
    Optional<Book> findById(Long id);
}
