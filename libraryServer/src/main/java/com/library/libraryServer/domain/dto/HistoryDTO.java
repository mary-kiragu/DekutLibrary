package com.library.libraryServer.domain.dto;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.library.libraryServer.domain.enums.*;

import java.time.*;

public interface HistoryDTO {
    public Long getId();
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getCreatedOn();

    public Actions getAction();

    public UserView getUser();

    public BookView getBook();

    public interface UserView {
        public Long getId();

        public String getEmail();
    }

    public interface BookView {
        public Long getId();

        public String getTitle();
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        public LocalDateTime getBorrowedOn();
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        public LocalDateTime getReturnedOn();
    }

}
