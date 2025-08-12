package com.example.controller;

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

    @GetMapping("/allBooks")
    public ResponseEntity<List<BookEntityToDto>> getAllBooks() {
        return new ResponseEntity<List<BookEntityToDto>>(bookService.allBooks(), HttpStatus.OK);
    }

    @GetMapping("/getBook/{bookId}")
    public ResponseEntity<BookEntityToDto> getBook(@PathVariable Long bookId) {
        return new ResponseEntity<BookEntityToDto>(bookService.getBook(bookId), HttpStatus.OK);
    }
}