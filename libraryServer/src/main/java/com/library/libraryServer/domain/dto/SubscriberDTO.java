package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class SubscriberDTO {
    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private List<ProfileDTO> profiles;

    public SubscriberDTO() {
    }


    public SubscriberDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();

        this.profiles = new ArrayList<>();
    }
}
