package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.security.jwt.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
@Slf4j
public class DiscussionService {
    private  final DiscussionRepository discussionRepository;

    public DiscussionService(DiscussionRepository discussionRepository) {
        this.discussionRepository = discussionRepository;
    }


    public Discussion save(Discussion comment) {
        log.debug("Request to save discussion : {}", comment);
        if (comment.getId() == null) {
            comment.setCreatedOn(LocalDateTime.now().toString());
            comment.setCreatedBy(SecurityUtils.getCurrentUserLogin2().orElse(null));

        } else {
            comment.setLastUpdatedOn(LocalDateTime.now().toString());
            comment.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin2().orElse(null));
        }
        return  discussionRepository.save(comment);
    }


    public List<DiscussionDTO> getCommentByBook(Long book) {
        List<DiscussionDTO> commentDTOList=discussionRepository.findByBook(book);
        return  commentDTOList;
    }
}
