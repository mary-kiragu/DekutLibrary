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
import org.springframework.transaction.annotation.*;

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

    private final BorrowHistoryRepository borrowHistoryRepository;

    private final MpesaConfiguration mpesaConfiguration;

    private final MailService mailService;


    public BookService(BookRepository bookRepository, BookMapper bookMapper, UserRepository userRepository, UserService userService, BorrowHistoryRepository borrowHistoryRepository, MpesaConfiguration mpesaConfiguration, MailService mailService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.borrowHistoryRepository = borrowHistoryRepository;
        this.mpesaConfiguration = mpesaConfiguration;
        this.mailService = mailService;
    }

    public List<Book> getAllBooks() {
        log.info("-------------------------------------------------------------------------");
        List<Book> allBooks = bookRepository.findAll();
        for (Book book : allBooks) {
            if (book.getIssuedOn() != null && book.getDueDate() != null) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                log.info("due date {}",book.getDueDate());
                LocalDateTime bookDueDate = book.getDueDate();
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(book.getDueDate(), now));

                if (now.compareTo(bookDueDate) > 0) {
                    long compute = daysBetween * 10;
                    book.setFine((int) compute);
                }

            }
            // bookDTOList.add(bookMapper.toDTO(book));
        }

        return allBooks;
    }

    public Book addNewBook(Book book) {
        log.info("add a new book to database");
        if (book.getId() == null) {
            book.setStatus(Status.AVAILABLE);
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
        log.info("about to grt book with id: {}", id);
        Optional<Book> bookOptional = bookRepository.findById(id);
        log.info("book found {}", bookOptional);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getIssuedOn() != null && book.getDueDate() != null) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime bookDueDate = book.getDueDate();
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(book.getDueDate(), now));

                if (now.compareTo(bookDueDate) > 0) {
                    long compute = daysBetween * 10;
                    book.setFine((int) compute);
                    //book.setFine(1);
                }

            }

        }
        return bookOptional;
    }

    //issue
    public Optional<Book> borrowBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setStatus(Status.BORROWED);
            User user = userService.getCurrentLoggedInUser();

            log.info("looged in user", user);

            if (user == null) {

            }
            book.setBorrowedOn(LocalDateTime.now());
            book.setReturnedOn(null);
            log.info("current user email", SecurityUtils.getCurrentUserLogin());
            book.setBorrowedBy(SecurityUtils.getCurrentUserLogin().orElse(null));
            book = bookRepository.save(book);

            return Optional.of(book);
        }


        return bookOptional;
    }

    public List<HistoryDTO> getHistoryByUser(Long userId) {
        log.info("About to get all books borrowed by user : {}", userId);
        List<HistoryDTO> borrowHistoryList = borrowHistoryRepository.findByUserId(userId);

        return borrowHistoryList;
    }

   @Scheduled(cron = "0 7 0 * * *")
   @Transactional
    public void releaseBook() {
        log.info("------------------------- ");

        LocalDateTime now = LocalDateTime.now();
        List<Book> books = getAllBooks();
        for (Book book : books) {
            if (book.getBorrowedOn() != null) {
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(book.getBorrowedOn(), now));
                log.info("Day difference : {}", daysBetween);
                if (daysBetween >=2 && book.getStatus()!=Status.ISSUED){
                    book.setStatus(Status.AVAILABLE);
                    book.setBorrowedBy(null);
                    book.setBorrowedOn(null);
                }

            }
        }
    }

    //borrow book
    public Optional<Book> issueBook(Long id) throws BorrowedBookException {
        Optional<Book> bookOptional = bookRepository.findById(id);


        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setStatus(Status.ISSUED);


            if (book.getBorrowedBy() == null) {
                throw new BorrowedBookException("Book not available for borrowing");

            }
            //User user = userService.getCurrentLoggedInUser();

            User user = userService.findByEmail(book.getBorrowedBy()).orElseThrow();

            log.info("looged in user", user);

            if (user == null) {

            }

            book.setIssuedOn(LocalDateTime.now());
            book.setReturnedOn(null);
            log.info("current user email", SecurityUtils.getCurrentUserLogin());
            book.setIssuedBy(SecurityUtils.getCurrentUserLogin().orElse(null));

            BorrowHistory borrowHistory = new BorrowHistory();

            assert user != null;
            if (user.getAuthority().equals(Authority.STUDENT)) {
                book.setDueDate(LocalDateTime.now().plusDays(14));
                borrowHistory.setDueDate(LocalDateTime.now().plusDays(14));

            } else if (user.getAuthority().equals(Authority.STAFF)) {

                book.setDueDate(LocalDateTime.now().plusDays(21));
                borrowHistory.setDueDate(LocalDateTime.now().plusDays(21));


            } else if (user.getAuthority().equals(Authority.SUBSCRIBER)) {

                book.setDueDate(LocalDateTime.now().plusDays(30));
                borrowHistory.setDueDate(LocalDateTime.now().plusDays(30));


            }

            log.info("due date: {}", book.getDueDate());

            book = bookRepository.save(book);
            user = userService.save(user);
            //BorrowHistory borrowHistory = new BorrowHistory();
            borrowHistory.setBook(book);
            borrowHistory.setUser(user);
            borrowHistory.setAction(Actions.BORROW);
            borrowHistory.setDueDate(book.getDueDate());
            borrowHistory.setCreatedOn(LocalDateTime.now());
            log.info("borrow hist {}", borrowHistory);
            borrowHistoryRepository.save(borrowHistory);

            return Optional.of(book);
        }

        return bookOptional;
    }


    public Optional<Book> returnBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);


        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setStatus(Status.AVAILABLE);
            User user = userService.findByEmail(book.getBorrowedBy()).orElseThrow();

            log.info("looged in user", user);

            if (user == null) {

            }

            if (book.getBorrowedBy() != null) {
                book.setBorrowedBy(null);
                book.setBorrowedOn(null);
                book.setIssuedBy(null);
                book.setDueDate(null);
                book.setFine(0);
                book.setIssuedOn(null);
                book.setReturnedOn(LocalDateTime.now());

            }

            book = bookRepository.save(book);
            //user=userService.update(user);
            BorrowHistory borrowHistory = new BorrowHistory();
            borrowHistory.setBook(book);
            borrowHistory.setUser(user);
            borrowHistory.setAction(Actions.RETURN);
            borrowHistory.setCreatedOn(LocalDateTime.now());
            log.info("borrow hist {}", borrowHistory);
            borrowHistoryRepository.save(borrowHistory);
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

    public Book updateBook(Book book) throws UserNotFoundException {
        Optional<Book> bookOptional = bookRepository.findById(book.getId());

        if (bookOptional.isPresent()) {
            log.info("Book found");
            Book bookDetails = bookOptional.get();
            bookDetails.setTitle(book.getTitle());
            bookDetails.setAuthor(book.getAuthor());
            bookDetails.setDescription(book.getDescription());
            bookDetails.setAccessionNumber(book.getAccessionNumber());
            bookDetails.setIsbn(book.getIsbn());
            return bookRepository.save(bookDetails);
        }else {
            log.info("book found");
            throw new UserNotFoundException("No internship found with id ");
        }



    }

    public List<BookDTO> filterByCategoryId(Integer categoryId) {
        log.info("About to get all books in category : {}", categoryId);
        List<BookDTO> bookDTOList = new ArrayList<>();

        for (Book book : bookRepository.findByCategoryId(categoryId)) {
            if (book.getIssuedOn() != null && book.getDueDate() != null) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime bookDueDate = book.getDueDate();
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(book.getDueDate(), now));

                if (now.compareTo(bookDueDate) > 0) {
                    long compute = daysBetween * 10;
                    book.setFine((int) compute);
                   // book.setFine(1);
                }

            }
            bookDTOList.add(bookMapper.toDTO(book));
        }

        log.info("books {}", bookDTOList);

        return bookDTOList;
    }


    public List<BookDTO> filterByUser(String borrowedBy) {
        log.info("About to get all books borrowed by user : {}", borrowedBy);
        List<BookDTO> bookDTOList = new ArrayList<>();

        for (Book book : bookRepository.findByBorrowedBy(borrowedBy)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // LocalDate bookDueDate = LocalDate.parse(book.getDueDate(), formatter);

            long daysBetween = Math.abs(ChronoUnit.DAYS.between(now, book.getDueDate()));

            if (now.compareTo(book.getDueDate()) > 0) {
                long compute = daysBetween * 1;
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

    public void sendDueDateReminder() {
        log.info("------------------------- ");

        log.info("Sending due date reminders");


        LocalDateTime now = LocalDateTime.now();

        short paymentReminder = mpesaConfiguration.getPaymentReminder();

        List<Book> books = getAllBooks();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Book book : books) {
            if (book.getBorrowedOn() != null) {
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(book.getDueDate(), now));
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


