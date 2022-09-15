package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.mapper.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.security.jwt.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
@Slf4j
public class BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.userRepository = userRepository;
    }

    public List<Book> getAllBooks() {
        List<Book> allBooks = bookRepository.findAll();
        log.info("This is a list of all books found: ", allBooks);
        return allBooks;
    }

    public Book addNewBook(Book book) {
        log.info("add a new book to database");
        if(book.getId()==null){
            book.setStatus(Status.NEW);
            book.setCopies(1L);

        }


        return bookRepository.save(book);
    }

    public Optional<Book> getOneBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
           if(book.getBorrowedBy() !=null) {
               book.setFine(book.getFine());
           }

        }
        return bookOptional;
    }


    //borrow book
    public Optional<Book> borrowBook(Long id) throws BorrowedBookException {
        Optional<Book> bookOptional = bookRepository.findById(id);

        LocalDate dueDate;

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setStatus(Status.BORROWED);

            User user = new User();

            if (book.getBorrowedBy() != null) {
                throw new BorrowedBookException("Book not available for borrowing");

            }

//            if(user.getAuthority().equals(Authority.STUDENT)){
//
//                dueDate= LocalDate.parse(book.getBorrowedOn()).plusDays(14);
//
//            } else if (user.getAuthority().equals(Authority.STAFF)) {
//
//                dueDate= LocalDate.parse(book.getBorrowedBy()).plusDays(21);
//
//            }else {
//
//                dueDate= LocalDate.parse(book.getBorrowedBy()).plusDays(30);
//
//            }
//
//            log.info("due date: {}",dueDate);


            book.setBorrowedOn(String.valueOf(LocalDateTime.now()));
            book.setReturnedOn(null);
            log.info("current user email",SecurityUtils.getCurrentUserLogin());
            book.setBorrowedBy(SecurityUtils.getCurrentUserLogin().orElse(null));
            //compute fine.



            book = bookRepository.save(book);
            return Optional.of(book);
        }

        return bookOptional;
    }


    public Optional<Book> returnBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setStatus(Status.RETURNED);

            if (book.getBorrowedBy() != null) {
                book.setBorrowedBy(null);
                book.setBorrowedOn(null);
                book.setReturnedOn(String.valueOf(LocalDateTime.now()));

            }

            book = bookRepository.save(book);
            return Optional.of(book);
        }

        return bookOptional;
    }

    public Book deleteBook(Long id) {
        boolean exist = bookRepository.existsById(id);
        if (!exist) {
            throw new IllegalStateException("Book doesn't exist");

        }
        bookRepository.deleteById(id);
        return null;
    }

    public Optional<Book> updateBook(Long id, String title, String author, String imageUrl) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        log.info("Book found with id {}",id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (id !=null && id > 0 && !Objects.equals(bookOptional.get(), id)) {
                book.setId(id);

            }
            if (title != null && title.length() > 0 && !Objects.equals(bookOptional.get(), title)) {
                book.setTitle(title);

            }
            if (author != null && author.length() > 0 && !Objects.equals(bookOptional.get(), author)) {
                book.setAuthor(author);

            }
            if (imageUrl != null && imageUrl.length() > 0 && !Objects.equals(bookOptional.get(), imageUrl)) {
                book.setImageUrl(imageUrl);

            }
            book=bookRepository.save(book);
            log.info("Updated book with id {}:", book.getId());
            return Optional.of(book);
        }

        return bookOptional;

    }

    public List<BookDTO> filterByCategoryId(Integer categoryId) {
        log.info("About to get all books in category : {}", categoryId);
        List<BookDTO> bookDTOList = new ArrayList<>();

        for (Book book : bookRepository.findByCategoryId(categoryId)) {
            bookDTOList.add(bookMapper.toDTO(book));
        }

        log.info("books {}",bookDTOList);

        return bookDTOList;
    }


    public List<BookDTO> filterByUser(String borrowedBy) {
        log.info("About to get all books borrowed by user : {}", borrowedBy);
        List<BookDTO> bookDTOList = new ArrayList<>();

        for (Book book : bookRepository.findByBorrowedBy(borrowedBy)) {
            bookDTOList.add(bookMapper.toDTO(book));
        }

        return bookDTOList;
    }

    public List<Book> search(String text) {
        log.debug("Request to search books with text : {}", text);
        List<Book> books = bookRepository.findByTitleContainingOrAuthorContaining(text, text);

        return books;
    }
}


