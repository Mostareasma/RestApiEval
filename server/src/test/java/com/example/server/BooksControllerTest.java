package com.example.server;

import com.example.server.exceptions.BookNotFoundException;
import com.example.server.exceptions.InvalidRequestException;
import com.example.server.models.Book;
import com.example.server.repositories.BooksRepository;
import com.example.server.services.BooksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BooksControllerTest {

    @Autowired
    BooksRepository booksRepository;
    @MockBean
    BooksService bookService;
    @Autowired
    private MockMvc mockMvc;
    public static Book book;
    public static Book InvalidBook1;
    public static Book InvalidBook2;
    public static Book InvalidBook3;
    public static Book InvalidBook4;
    public static List<Book> books;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp(){
        Date fixedDate = new Date(30L);
        book = new Book(1L, "test", "SomeOne");
        InvalidBook1 = new Book(null, "test", "SomeOne");
        InvalidBook2 = new Book(1L, null, "SomeOne");
        InvalidBook3 = new Book(1L, "test", null);
        books = List.of(
                new Book(1L, "book1", "Some one"),
                new Book(2L, "book2", "Some on")
        );
    }


    @Test
    public void givenEmptyIsbn_whenSave_thenThrowsInvalidRequestException() throws Exception {

        when(bookService.save(book)).thenThrow(new InvalidRequestException("ISBN is required"));

        mockMvc.perform(post("/books")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.equalTo("ISBN is required")));

        verify(bookService, times(1)).save(book);
    }

    @Test
    public void givenEmptyTitle_whenSave_thenThrowsInvalidRequestException() throws Exception {

        when(bookService.save(book)).thenThrow(new InvalidRequestException("Title is required"));

        mockMvc.perform(post("/books")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.equalTo("Title is required")));

        verify(bookService, times(1)).save(book);
    }

    @Test
    public void givenEmptyAuthor_whenSave_thenThrowsInvalidRequestException() throws Exception {

        when(bookService.save(book)).thenThrow(new InvalidRequestException("Author is required"));

        mockMvc.perform(post("/books")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.equalTo("Author is required")));

        verify(bookService, times(1)).save(book);
    }

    @Test
    public void givenEmptyDate_whenSave_thenThrowsInvalidRequestException() throws Exception {

        when(bookService.save(book)).thenThrow(new InvalidRequestException("Publication date is required"));

        mockMvc.perform(post("/books")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.equalTo("Publication date is required")));

        verify(bookService, times(1)).save(book);
    }

    @Test
    public void givenValidBook_whenSave_thenStatus200() throws Exception {

        when(bookService.save(book)).thenReturn(book);

        mockMvc.perform(post("/books")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(book));

        verify(bookService, times(1)).save(book);
    }

    @Test
    public void whenGetAll_thenStatus200() throws Exception {

        when(bookService.getAll()).thenReturn(books);

        ObjectMapper objectMapper = new ObjectMapper();
        String booksJson = objectMapper.writeValueAsString(books);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(booksJson));

        verify(bookService, times(1)).getAll();
    }

    @Test
    public void givenExistentBookId_whenGetByIt_thenStatus200() throws Exception {

        when(bookService.getById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(book));

        verify(bookService, times(1)).getById(1L);
    }

    @Test
    public void givenNonexistentBookId_whenGetByIt_thenThrowsBookNotFoundException() throws Exception {

        when(bookService.getById(11L)).thenThrow(new BookNotFoundException("Book doesn't exist"));

        mockMvc.perform(get("/books/{id}", 11)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.equalTo("Book doesn't exist")));

        verify(bookService, times(1)).getById(11L);
    }

    @Test
    public void givenExistentBook_whenDelete_thenStatus200() throws Exception {

        when(bookService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/books/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(true)));

        verify(bookService, times(1)).delete(1L);
    }

    @Test
    public void givenExistentBook_whenDelete_thenThrowsBookNotFoundException() throws Exception {
        when(bookService.delete(14L)).thenThrow(new BookNotFoundException("Book doesn't exist"));

        mockMvc.perform(delete("/books/{id}", 14))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.equalTo("Book doesn't exist")));

        verify(bookService, times(1)).delete(14L);
    }
}
