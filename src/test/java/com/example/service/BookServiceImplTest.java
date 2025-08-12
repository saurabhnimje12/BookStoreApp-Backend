package com.example.service;

import com.example.dto.BookEntityToDto;
import com.example.dto.DtoToBookEntity;
import com.example.entity.Book;
import com.example.exception.CustomiseException;
import com.example.repo.BookRepo;
import com.example.serviceImpl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- addBook ----------
    @Test
    void testAddBook_Success() {
        DtoToBookEntity dto = createSampleDto();
        when(bookRepo.save(any(Book.class))).thenReturn(new Book());

        String result = bookService.addBook(dto);

        assertEquals("Book Added Successfully", result);
        verify(bookRepo, times(1)).save(any(Book.class));
    }

    // ---------- deleteBook ----------
    @Test
    void testDeleteBook_Success() {
        doNothing().when(bookRepo).deleteById(1L);

        String result = bookService.deleteBook(1L);

        assertEquals("Book Deleted Successfully", result);
        verify(bookRepo, times(1)).deleteById(1L);
    }

    // ---------- allBooks ----------
    @Test
    void testAllBooks_Success() {
        Book book1 = createSampleBook(1L);
        Book book2 = createSampleBook(2L);
        when(bookRepo.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<BookEntityToDto> result = bookService.allBooks();

        assertEquals(2, result.size());
        assertEquals(book1.getBookName(), result.get(0).getBookName());
        verify(bookRepo, times(1)).findAll();
    }

    // ---------- getBook ----------
    @Test
    void testGetBook_ValidId() {
        Book book = createSampleBook(1L);
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        BookEntityToDto result = bookService.getBook(1L);

        assertEquals(book.getBookName(), result.getBookName());
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void testGetBook_InvalidId_ThrowsException() {
        when(bookRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomiseException.class, () -> bookService.getBook(99L));
        verify(bookRepo, times(1)).findById(99L);
    }

    // ---------- updateBook ----------
    @Test
    void testUpdateBook_ValidId_Success() {
        Book existingBook = createSampleBook(1L);
        DtoToBookEntity dto = createSampleDto();
        when(bookRepo.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepo.save(any(Book.class))).thenReturn(existingBook);

        String result = bookService.updateBook(1L, dto);

        assertEquals("Book Updated Successfully", result);
        verify(bookRepo, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBook_InvalidId_ReturnsErrorMessage() {
        DtoToBookEntity dto = createSampleDto();
        when(bookRepo.findById(99L)).thenReturn(Optional.empty());

        String result = bookService.updateBook(99L, dto);

        assertEquals("Invalid Book Id!!", result);
        verify(bookRepo, never()).save(any(Book.class));
    }

    // ---------- Helper methods ----------
    private DtoToBookEntity createSampleDto() {
        DtoToBookEntity dto = new DtoToBookEntity();
        dto.setBookName("Java Basics");
        dto.setBookAuthor("John Doe");
        dto.setBookDescription("Intro to Java");
        dto.setBookLogoMultipart("logo.png");
        dto.setBookPrice(500.0);
        dto.setBookQuantity(10);
        return dto;
    }

    private Book createSampleBook(Long id) {
        Book book = new Book();
        book.setBookId(id);
        book.setBookName("Java Basics");
        book.setBookAuthor("John Doe");
        book.setBookDescription("Intro to Java");
        book.setBookLogoMultipart("logo.png");
        book.setBookPrice(500.0);
        book.setBookQuantity(10);
        return book;
    }
}
