package com.library.libraryServer.domain.dto;

import java.time.*;

public interface DiscussionDTO {
    public Long getId();

    public String getContent();

    public String getCreatedOn();
    public String getCreatedBy();


    public Long getBook();

}
