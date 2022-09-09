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
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setCategoryId(book.getCategoryId());
        bookDTO.setBorrowedBy(bookDTO.getBorrowedBy());
        bookDTO.setReturnedOn(bookDTO.getReturnedOn());






        return bookDTO;
    }
}
