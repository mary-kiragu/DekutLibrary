package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.resource.vms.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.*;

import java.util.*;

@RestController
@RequestMapping(path = "/api/books")
@Slf4j
public class BookResource {
    private final BookService bookService;


    public BookResource(BookService bookService) {
        this.bookService = bookService;

    }

    @GetMapping
    ResponseEntity<List<Book>> getAll() {
        List<Book> allBooks = bookService.getAllBooks();
        for (Book book : allBooks){
            book.setBookImageUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/books/downloadProfilePicture/")
                    .path(String.valueOf(book.getId()))
                    .toUriString());
            book.setBookUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/books/downloadBook/")
                    .path(String.valueOf(book.getId()))
                    .toUriString());
//            System.out.println("Got file"+ book);
        }


        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @GetMapping("/history/{user}")
    ResponseEntity<List<HistoryDTO>> borrowHistoryByUser(@PathVariable User user) {
        List<HistoryDTO>  historyList = bookService.getHistoryByUser(user.getId());
        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Book> addBook(@RequestBody Book book) {
        log.info("request to add new book");
        book.setBookImageUrl(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/books/downloadProfilePicture/")
                .path(String.valueOf(book.getId()))
                .toUriString());
        book.setBookUrl(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/books/downloadBook/")
                .path(String.valueOf(book.getId()))
                .toUriString());
        Book newBook = bookService.addNewBook(book);

        return new ResponseEntity<>(newBook, HttpStatus.OK);
    }
    @GetMapping("/downloadProfilePicture/{id}")
    public ResponseEntity<byte[]> downloadProfilePicture(@PathVariable Long id) {
        Book book = bookService.downloadProfilePicture((id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + book.getName() + "\"")
                .body(book.getData());
    }
    @GetMapping("/downloadBook/{id}")
    public ResponseEntity<byte[]> downloadBook(@PathVariable Long id) {
        Book book = bookService.downloadBook((id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + book.getBookName() + "\"")
                .body(book.getBookData());
    }




    @GetMapping(path = "{id}")
    ResponseEntity<Book> getOneBook(@PathVariable("id") Long id) {

        Optional<Book> bookOptional = bookService.getOneBook(id);


        Book book = null;
        if (bookOptional.isPresent()) {
            book = bookOptional.get();
            book.setBookImageUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/books/downloadProfilePicture/")
                    .path(String.valueOf(book.getId()))
                    .toUriString());
            book.setBookUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/books/downloadBook/")
                    .path(String.valueOf(book.getId()))
                    .toUriString());
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // function to borrow
    @PutMapping(path="/issue/{id}")
    ResponseEntity issueBook(@RequestBody Book book) {
        Optional<Book> issuedBook = null;
        try {
            issuedBook = bookService.issueBook(book.getId());
        } catch (BorrowedBookException e) {
            RegisterUserVM.Errorvm errorvm=new RegisterUserVM.Errorvm(e.getMessage(),400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorvm);

        }
        return new ResponseEntity(issuedBook, HttpStatus.OK);

    }
    @PutMapping(path="/borrow/{id}")
    ResponseEntity borrowBook(@PathVariable("id") Long id) {
        Optional<Book> borrowedBook = null;

            borrowedBook = bookService.borrowBook(id);

        return new ResponseEntity(borrowedBook, HttpStatus.OK);

    }
    @PutMapping(path ="/return/{id}")
    ResponseEntity returnBook(@RequestBody Book book){
        Optional<Book> returnedBook=null;
        returnedBook=bookService.returnBook(book.getId());
        return new ResponseEntity(returnedBook,HttpStatus.OK);
    }

    @PutMapping(path ="/update")
    ResponseEntity updateBook(@RequestBody Book book) throws UserNotFoundException {
        System.out.println("========================================================================================");
        log.info("Hit update route{}",book.getDescription());
        System.out.println("cnsjdnmckdsmckmk");
        Book updatedBook=  bookService.updateBook(book);

        return new ResponseEntity(updatedBook,HttpStatus.OK);
    }

    @DeleteMapping(path="{id}")
    ResponseEntity deleteBook(@PathVariable("id") Long id){
       Book deletedBook=bookService.deleteBook(id);
        return new ResponseEntity(deletedBook,HttpStatus.OK);
    }

    @GetMapping(path="/filter-by-category/{categoryId}")
    public List<BookDTO> filterByParentCategory(@PathVariable Integer categoryId) {
        log.info("Request to filter by category");

        List<BookDTO> bookDTOS = bookService.filterByCategoryId(categoryId);
        for (BookDTO bookDto : bookDTOS){
            bookDto.setBookImageUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/books/downloadProfilePicture/")
                    .path(String.valueOf(bookDto.getId()))
                    .toUriString());
            bookDto.setBookUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/books/downloadBook/")
                    .path(String.valueOf(bookDto.getId()))
                    .toUriString());
            System.out.println("Got file"+ bookDto);
        }

        log.info("saved to filter by parent {}",bookDTOS);


        return bookDTOS;
    }

    @GetMapping("/filter-by-user/{borrowedBy}")
    public List<BookDTO> filterByParentCategory(@PathVariable String borrowedBy) {

        log.debug("about to get books borrowed by user {}",borrowedBy);

        List<BookDTO>bookDTOS = bookService.filterByUser(borrowedBy);

        return bookDTOS;
    }

    @GetMapping(path= "/search")
    public List<Book> findAll(@RequestParam(required = false) String text) {
        log.debug("REST request to search all books with text : {}", text);

        if (text  == null) {
            text = "";
        }

        return bookService.search(text);
    }





}
