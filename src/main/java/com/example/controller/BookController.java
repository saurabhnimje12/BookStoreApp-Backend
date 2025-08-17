package com.example.controller;

/**
 * BookController.java
 * This controller handles requests related to books.
 * It provides endpoints to retrieve all books and a specific book by its ID.
 */

import com.example.dto.BookEntityToDto;
import com.example.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "http://localhost:5173")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves all books.
     *
     * @return a ResponseEntity containing a list of all books
     */
    @GetMapping("/allBooks")
    public ResponseEntity<List<BookEntityToDto>> getAllBooks() {
        return new ResponseEntity<List<BookEntityToDto>>(bookService.allBooks(), HttpStatus.OK);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId the ID of the book to retrieve
     * @return a ResponseEntity containing the book details or an error message if not found
     */
    @GetMapping("/getBook/{bookId}")
    public ResponseEntity<BookEntityToDto> getBook(@PathVariable Long bookId) {
        return new ResponseEntity<BookEntityToDto>(bookService.getBook(bookId), HttpStatus.OK);
    }
}