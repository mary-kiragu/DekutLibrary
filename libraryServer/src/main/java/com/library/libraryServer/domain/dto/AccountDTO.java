package com.library.libraryServer.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountDTO {
    private Long id;

    private String name;

    private String email;
}
