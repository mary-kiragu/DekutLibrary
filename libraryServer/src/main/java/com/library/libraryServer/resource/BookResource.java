package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.resource.vms.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/api/books")
@Slf4j
public class BookResource {
    private final BookService bookService;

    @Autowired
    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    ResponseEntity<List<Book>> getAll() {
        List<Book> allBooks = bookService.getAllBooks();
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Book> addBook(@RequestBody Book book) {
        log.info("request to add new book");
        Book newBook = bookService.addNewBook(book);
        return new ResponseEntity<>(newBook, HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    ResponseEntity<Book> getOneBook(@PathVariable("id") Long id) {
        Optional<Book> bookOptional = bookService.getOneBook(id);
        Book book = null;
        if (bookOptional.isPresent()) {
            book = bookOptional.get();
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // function to borrow
    @PutMapping(path="/borrow/{id}")
    ResponseEntity borrowBook(@PathVariable("id") Long id) {
        Optional<Book> borrowedBook = null;
        try {
            borrowedBook = bookService.borrowBook(id);
        } catch (BorrowedBookException e) {
            RegisterUserVM.Errorvm errorvm=new RegisterUserVM.Errorvm(e.getMessage(),400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorvm);

        }
        return new ResponseEntity(borrowedBook, HttpStatus.OK);

    }

    @PutMapping(path ="/return/{id}")
    ResponseEntity returnBook(@RequestBody Book book){
        Optional<Book> returnedBook=null;
        returnedBook=bookService.returnBook(book.getId());
        return new ResponseEntity(returnedBook,HttpStatus.OK);
    }

    @PutMapping(path ="/update/{id}")
    ResponseEntity updateBook(@RequestBody Book book){
        Optional<Book> updatedBook=null;
        updatedBook=bookService.updateBook(book.getId(), book.getTitle(), book.getAuthor(), book.getImageUrl());
        return new ResponseEntity(updatedBook,HttpStatus.OK);
    }

    @DeleteMapping(path="{id}")
    ResponseEntity deleteBook(@PathVariable("id") Long id){
       Book deletedBook=bookService.deleteBook(id);
        return new ResponseEntity(deletedBook,HttpStatus.OK);
    }




}
