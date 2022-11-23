package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.security.jwt.*;
import liquibase.pro.packaged.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
@Slf4j
public class DiscussionService {
    private final DiscussionRepository discussionRepository;

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
        return discussionRepository.save(comment);

    }


    public List<DiscussionDTO> getCommentByBook(Long book) {
        List<DiscussionDTO> commentDTOList = discussionRepository.findByBook(book);
        return commentDTOList;
    }

    public Optional<Discussion> findById(Long id) {
        log.info("about to find comment with id{}",id);
        Optional<Discussion> discussionOptional = discussionRepository.findById(id);
        log.info("found comment with id{} value{}",id,discussionOptional);
        if (discussionOptional.isPresent()) {
            Discussion discussion = discussionOptional.get();
        }

        return discussionOptional;
    }

    public List<DiscussionDTO> filterByReferenceCommentId(Long referencedCommentId) {
        log.info("About to get all books in category : {}", referencedCommentId);

        List<DiscussionDTO> discussionDTOList = new ArrayList<>();
        discussionDTOList.add(discussionRepository.findByReferencedCommentId(referencedCommentId));

        return  discussionDTOList;


}

public Optional<Discussion >update(Long id){
    Optional<Discussion> discussionOptional = discussionRepository.findById(id);
    log.info("Book found with id {}", id);
    if(discussionOptional.isPresent()){
    Discussion discussion=discussionOptional.get();
    if (discussion.getContent() != null && discussion.getContent().length() > 0 && !Objects.equals(discussionOptional.get(), discussion.getContent())) {
        discussion.setContent(discussion.getContent());

    }
       discussion= discussionRepository.save(discussion);
        return Optional.of(discussion);

    }


    return discussionOptional;

}

    public Discussion deleteDiscussion(Long id) {
        log.info("about to delete discussion {}",id);
        boolean exist = discussionRepository.existsById(id);
        if (!exist) {
            throw new IllegalStateException("comment doesn't exist");

        }
        discussionRepository.deleteById(id);
        return null;
    }
}
