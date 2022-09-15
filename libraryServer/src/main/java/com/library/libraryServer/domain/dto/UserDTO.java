package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;

@Data
public class UserDTO {
    private Long id;

    private String email;

    private String name;

    private String password;

    private String phoneNumber;

    private Authority authority;
}
