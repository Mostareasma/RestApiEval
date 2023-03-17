package com.example.server.services;
import com.example.server.exceptions.BookNotFoundException;
import com.example.server.exceptions.InvalidRequestException;
import com.example.server.models.Book;
import com.example.server.repositories.BooksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BooksService {

    @Autowired
    private BooksRepository booksRepository;
    public void validateBook(Book book) throws InvalidRequestException {
        if (book.getIsbn() == null || book.getIsbn() == null) {
            throw new InvalidRequestException("ISBN is required");
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidRequestException("Title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new InvalidRequestException("Author is required");
        }
    }

    public Book save(Book book) throws InvalidRequestException {
        validateBook(book);
        return booksRepository.save(book);
    }


    public Book update(Book book) throws BookNotFoundException , InvalidRequestException {

        try {
            Book existingBook = getById(book.getIsbn());

            validateBook(book);

            return booksRepository.save(book);

        } catch (Exception e) {
            throw new BookNotFoundException("Book doesn't exist");
        }
    }


    public List<Book> getAll() {
        return booksRepository.findAll();
    }

    public Book getById(Long id) throws BookNotFoundException {
        Optional<Book> optionalBook = booksRepository.findById(id);
        if (!optionalBook.isPresent()) {
            throw new BookNotFoundException("Book doesn't exist");
        }
        return optionalBook.get();
    }

    public Boolean delete(Long id) throws BookNotFoundException {
        Book existingBook = getById(id);

        booksRepository.delete(existingBook);
        return true;
    }

}