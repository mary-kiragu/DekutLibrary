package com.library.libraryServer.exceptions;

public class BorrowedBookException  extends Exception{
    public BorrowedBookException() {
    }

    public BorrowedBookException(String message) {
        super(message);
    }

    public BorrowedBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public BorrowedBookException(Throwable cause) {
        super(cause);
    }

    public BorrowedBookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
