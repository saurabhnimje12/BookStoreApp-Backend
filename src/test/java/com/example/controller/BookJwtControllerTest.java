package com.example.controller;

import com.example.dto.DtoToBookEntity;
import com.example.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookJwtControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookJwtController bookJwtController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- addBook ----------
    @Test
    void testAddBook_AdminRole_Success() {
        DtoToBookEntity dto = new DtoToBookEntity();
        when(bookService.addBook(dto)).thenReturn("Book Added Successfully");

        ResponseEntity<String> response = bookJwtController.addBook("ADMIN", dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book Added Successfully", response.getBody());
        verify(bookService, times(1)).addBook(dto);
    }

    @Test
    void testAddBook_NonAdminRole_Failure() {
        DtoToBookEntity dto = new DtoToBookEntity();

        ResponseEntity<String> response = bookJwtController.addBook("USER", dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Token is NOT Valid to Add Book", response.getBody());
        verify(bookService, never()).addBook(any());
    }

    // ---------- deleteBook ----------
    @Test
    void testDeleteBook_AdminRole_Success() {
        when(bookService.deleteBook(1L)).thenReturn("Book Deleted Successfully");

        ResponseEntity<String> response = bookJwtController.addBook("ADMIN", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book Deleted Successfully", response.getBody());
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void testDeleteBook_NonAdminRole_Failure() {
        ResponseEntity<String> response = bookJwtController.addBook("USER", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Token is NOT Valid to Delete Book", response.getBody());
        verify(bookService, never()).deleteBook(anyLong());
    }

    // ---------- updateBook ----------
    @Test
    void testUpdateBook_AdminRole_Success() {
        DtoToBookEntity dto = new DtoToBookEntity();
        when(bookService.updateBook(1L, dto)).thenReturn("Book Updated Successfully");

        ResponseEntity<String> response = bookJwtController.updateBook("ADMIN", 1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book Updated Successfully", response.getBody());
        verify(bookService, times(1)).updateBook(1L, dto);
    }

    @Test
    void testUpdateBook_NonAdminRole_Failure() {
        DtoToBookEntity dto = new DtoToBookEntity();

        ResponseEntity<String> response = bookJwtController.updateBook("USER", 1L, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Token is NOT Valid to Update Book", response.getBody());
        verify(bookService, never()).updateBook(anyLong(), any());
    }
}
