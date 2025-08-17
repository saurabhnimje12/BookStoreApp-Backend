package com.example.controller;

/**
 * BookJwtController.java
 * This controller handles book-related operations with JWT authentication.
 * It allows adding, deleting, and updating books based on user roles.
 */

import com.example.dto.DtoToBookEntity;
import com.example.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookApi")
@CrossOrigin(origins = "http://localhost:5173")
public class BookJwtController {
    private BookService bookService;

    public BookJwtController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Adds a new book.
     * @param role the role of the user making the request
     * @param dtoToBookEntity the DTO containing book details
     * @return a ResponseEntity with a success message or an error message
     */
    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestAttribute("role") String role, @RequestBody DtoToBookEntity dtoToBookEntity) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(bookService.addBook(dtoToBookEntity), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Add Book", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a book by its ID.
     * @param role the role of the user making the request
     * @param bookId the ID of the book to delete
     * @return a ResponseEntity with a success message or an error message
     */
    @DeleteMapping("/deleteBook/{bookId}")
    public ResponseEntity<String> addBook(@RequestAttribute("role") String role, @PathVariable Long bookId) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(bookService.deleteBook(bookId), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Delete Book", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates a book's details.
     * @param role the role of the user making the request
     * @param bookId the ID of the book to update
     * @param dtoToBookEntity the DTO containing updated book details
     * @return a ResponseEntity with a success message or an error message
     */
    @PatchMapping("/updateBook/{bookId}")
    public ResponseEntity<String> updateBook(@RequestAttribute("role") String role, @PathVariable Long bookId, @RequestBody DtoToBookEntity dtoToBookEntity) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(bookService.updateBook(bookId, dtoToBookEntity), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Token is NOT Valid to Update Book", HttpStatus.NOT_FOUND);
        }
    }
}
