package com.library.libraryServer.mapper;

import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import org.springframework.stereotype.*;

@Service
public class BookMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();


    public BookDTO toDTO(Book book){
        if(book==null){
            return null;
        }

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setImageUrl(book.getImageUrl());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setStatus(book.getStatus());
        bookDTO.setCategoryId(book.getCategoryId());
        bookDTO.setBorrowedBy(book.getBorrowedBy());
        bookDTO.setReturnedOn(book.getReturnedOn());
        bookDTO.setFine(book.getFine());
        bookDTO.setAccessionNumber(book.getAccessionNumber());
        bookDTO.setDueDate(book.getDueDate());
        bookDTO.setBookUrl(book.getBookUrl());
        bookDTO.setBookData(book.getBookData());
        bookDTO.setBookName(book.getBookName());
        bookDTO.setBookType(book.getBookType());
        bookDTO.setBookSize(book.getBookSize());



        return bookDTO;
    }
}
