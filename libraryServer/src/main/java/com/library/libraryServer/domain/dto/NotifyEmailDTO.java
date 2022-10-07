package com.library.libraryServer.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyEmailDTO {

    private String receiverEmail;

    private String subject;

    private String body;

    private boolean isHtml;

    private boolean isMultiPart;
}
