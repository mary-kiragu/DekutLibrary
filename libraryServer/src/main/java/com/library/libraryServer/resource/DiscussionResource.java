package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path="/api/comments")
@Slf4j
public class DiscussionResource {
    private  final DiscussionService discussionService;

    public DiscussionResource(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }


    @PostMapping
    public Discussion createComment(@RequestBody Discussion comment) {
        log.debug("REST request to create comment : {}", comment);
        Discussion discussion=discussionService.save(comment);
        return discussion;
    }

    @GetMapping("/get/{book}")
   public List<DiscussionDTO> getCommentsByBook(@PathVariable("book") Long book){
        log.info("about to");
        List<DiscussionDTO> discussionDTOList=discussionService.getCommentByBook(book);
        return discussionDTOList;
    }


}
