package com.library.libraryServer.domain;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.time.*;

@Data
@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {
    //ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private Integer plan;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Long account;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate lastBillingDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate nextBillingDate;

    public String getAuthorityName() {
        if (this.authority != null) {
            return authority.name();
        }
        return "";
    }
    
}
