package com.library.libraryServer.services;

import com.library.libraryServer.config.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.mapper.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.security.jwt.*;
import com.library.libraryServer.util.*;
import lombok.extern.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

@Service
@Slf4j
public class BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;
    private final UserRepository userRepository;

    private final UserService userService;

    private final MpesaConfiguration mpesaConfiguration;

    private final MailService mailService;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, UserRepository userRepository, UserService userService, MpesaConfiguration mpesaConfiguration, MailService mailService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.mpesaConfiguration = mpesaConfiguration;
        this.mailService = mailService;
    }

    public List<Book> getAllBooks() {
        List<Book> allBooks = bookRepository.findAll();
        log.info("This is a list of all books found: ", allBooks);
        return allBooks;
    }

    public Book addNewBook(Book book) {
        log.info("add a new book to database");
        if (book.getId() == null) {
            book.setStatus(Status.NEW);
            book.setCopies(1L);

        }


        return bookRepository.save(book);
    }

    public Book downloadProfilePicture(Long id) {
        return bookRepository.findById(id).get();
    }
    public Book downloadBook(Long id) {
        return bookRepository.findById(id).get();
    }




    public List<Book> findBookByBorrowedBy(String borrowedBy) {
        User user = userService.getCurrentLoggedInUser();
        borrowedBy = user.getEmail();
        List<Book> books = bookRepository.findByBorrowedBy(borrowedBy);
        log.info("books borrowed by{}", borrowedBy);
        return books;

    }


    public Optional<Book> getOneBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getBorrowedBy() != null) {
                LocalDate now = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate bookDueDate = LocalDate.parse(book.getDueDate(), formatter);
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(book.getDueDate(), formatter), now));

                if (now.compareTo(bookDueDate) > 0) {
                    long compute = daysBetween * 10;
                    book.setFine((int) compute);
                }

            }

        }
        return bookOptional;
    }


    //borrow book
    public Optional<Book> borrowBook(Long id) throws BorrowedBookException {
        Optional<Book> bookOptional = bookRepository.findById(id);


        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setStatus(Status.BORROWED);


            if (book.getBorrowedBy() != null) {
                throw new BorrowedBookException("Book not available for borrowing");

            }
            User user = userService.getCurrentLoggedInUser();

            log.info("looged in user", user);

            if (user == null) {

            }

            book.setBorrowedOn(String.valueOf(LocalDateTime.now()));
            book.setReturnedOn(null);
            log.info("current user email", SecurityUtils.getCurrentUserLogin());
            book.setBorrowedBy(SecurityUtils.getCurrentUserLogin().orElse(null));


            assert user != null;
            if (user.getAuthority().equals(Authority.STUDENT)) {
                book.setDueDate(String.valueOf(LocalDateTime.now().plusDays(14)));

            } else if (user.getAuthority().equals(Authority.STAFF)) {

                book.setDueDate(String.valueOf(LocalDateTime.now().plusDays(21)));


            } else if (user.getAuthority().equals(Authority.SUBSCRIBER)) {

                book.setDueDate(String.valueOf(LocalDateTime.now().plusDays(30)));


            }

            log.info("due date: {}", book.getDueDate());

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
                book.setDueDate(null);
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
        log.info("Book found with id {}", id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (id != null && id > 0 && !Objects.equals(bookOptional.get(), id)) {
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
            book = bookRepository.save(book);
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

        log.info("books {}", bookDTOList);

        return bookDTOList;
    }


    public List<BookDTO> filterByUser(String borrowedBy) {
        log.info("About to get all books borrowed by user : {}", borrowedBy);
        List<BookDTO> bookDTOList = new ArrayList<>();

        for (Book book : bookRepository.findByBorrowedBy(borrowedBy)) {
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate bookDueDate = LocalDate.parse(book.getDueDate(), formatter);
            long daysBetween = Math.abs(ChronoUnit.DAYS.between(now, bookDueDate));

            if (now.compareTo(bookDueDate) > 0) {
                long compute = daysBetween * 10;
                book.setFine((int) compute);
            }
            bookDTOList.add(bookMapper.toDTO(book));
        }

        return bookDTOList;
    }

    public List<Book> search(String text) {
        log.debug("Request to search books with text : {}", text);
        List<Book> books = bookRepository.findByTitleContainingOrAuthorContaining(text, text);

        return books;
    }


    @Scheduled(cron = "0 7 0 * * *")
   // @Scheduled(fixedRate = 25000)

    public void sendDuedateReminder() {
        log.info("------------------------- ");

        log.info("Sending due date reminders");


        LocalDate now = LocalDate.now();

        short paymentReminder = mpesaConfiguration.getPaymentReminder();

        List<Book> books = getAllBooks();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Book book : books) {
            if (book.getBorrowedOn() != null) {
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(book.getDueDate(), formatter), now));
                log.info("Day difference : {}", daysBetween);
                if (daysBetween == paymentReminder) {
                    BookDTO bookDTO = bookMapper.toDTO(book);
                    log.info("Day difference dto : {}", bookDTO);
                    // send duedate reminder
                    log.info("About to send ");
                    String body = TemplateUtil.generateReturnBookReminder(paymentReminder, bookDTO);
                    NotifyEmailDTO notifyEmailDTO = new NotifyEmailDTO(book.getBorrowedBy(),
                            "Book Due date Reminder", body, true, false);

                    log.info("about to send email:{}", notifyEmailDTO);

                    mailService.sendEmail(notifyEmailDTO);
                }
            }
        }

    }

}


