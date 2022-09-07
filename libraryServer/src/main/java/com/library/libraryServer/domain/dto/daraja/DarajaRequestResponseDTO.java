package com.library.libraryServer.domain.dto.daraja;

import lombok.*;

@Data
public class DarajaRequestResponseDTO {

    private DarajaResponseDTO response;

    private DarajaError error;
}
