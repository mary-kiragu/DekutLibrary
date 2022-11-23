package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
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
    @GetMapping(path="{id}")
    public ResponseEntity<Discussion> findById(@PathVariable("id") Long id){
        log.info("about to find comment with id{}",id);
        Optional<Discussion> discussionOptional=discussionService.findById(id);
        Discussion discussion=discussionOptional.get();
        return  new ResponseEntity<>(discussion, HttpStatus.OK);
    }

    @GetMapping(path="/filter-by-referenceId/{referencedCommentId}")
    public List<DiscussionDTO> filterByParentCategory(@PathVariable Long referencedCommentId) {
        log.info("Request to filter by referencedCommentId");

        List<DiscussionDTO> discussionDTOList = discussionService.filterByReferenceCommentId(referencedCommentId);
        return  discussionDTOList;

    }
    @PutMapping(path="/update/{id}")
    ResponseEntity issueBook(@RequestBody Discussion discussion) {
        Optional<Discussion> updatedDiscussion=discussionService.update(discussion.getId());
        return new ResponseEntity(updatedDiscussion, HttpStatus.OK);
    }
    @DeleteMapping(path="{id}")
    ResponseEntity deleteDiscussion(@PathVariable("id") Long id){
        log.info("about to delete discussion {}",id);
        Discussion deletedDiscussion=discussionService.deleteDiscussion(id);
        log.info(" deleted discussion {}",deletedDiscussion);
        return new ResponseEntity(deletedDiscussion,HttpStatus.OK);
    }

}
