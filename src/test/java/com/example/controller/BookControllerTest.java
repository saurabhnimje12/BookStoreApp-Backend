package com.example.controller;

import com.example.dto.BookEntityToDto;
import com.example.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        List<BookEntityToDto> books = Arrays.asList(new BookEntityToDto(), new BookEntityToDto());
        when(bookService.allBooks()).thenReturn(books);

        // Act
        ResponseEntity<List<BookEntityToDto>> response = bookController.getAllBooks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
        verify(bookService, times(1)).allBooks();
    }

    @Test
    void testGetBook() {
        // Arrange
        BookEntityToDto book = new BookEntityToDto();
        book.setBookId(1L);
        book.setBookName("Test Book");

        when(bookService.getBook(1L)).thenReturn(book);

        // Act
        ResponseEntity<BookEntityToDto> response = bookController.getBook(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).getBook(1L);
    }
}
