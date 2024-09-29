package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Book;
import java.util.List;

public interface BookService {
    // 카테고리 타입과 카테고리별 책 목록 조회
    List<Book> findBooksByCategoryTypeAndCategory(String categoryType, String category);
}
