package com.example.server.controllers;

import com.example.server.exceptions.BookNotFoundException;
import com.example.server.exceptions.InvalidRequestException;
import com.example.server.models.Book;
import com.example.server.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksService booksService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Book> books = booksService.getAll();
        return ResponseEntity.ok().body(books);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Book book) throws InvalidRequestException {
        Book savedBook = booksService.save(book);
        return ResponseEntity.ok().body(savedBook);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getById(@PathVariable Long bookId) throws BookNotFoundException {
        Book book = booksService.getById(bookId);
        return ResponseEntity.ok().body(book);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> update(@PathVariable Long bookId, @RequestBody Book book) throws BookNotFoundException, InvalidRequestException {
        Book existingBook = booksService.getById(bookId);
        existingBook.setIsbn(book.getIsbn());
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        Book savedBook = booksService.update(existingBook);
        return ResponseEntity.ok().body(savedBook);
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> delete(@PathVariable Long bookId) throws BookNotFoundException {
        Boolean res = booksService.delete(bookId);
        return ResponseEntity.ok().body(res);
    }

}