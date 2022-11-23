package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface DiscussionRepository extends JpaRepository<Discussion,Long> {
    List<DiscussionDTO> findByBook(Long book);

    DiscussionDTO findByReferencedCommentId(Long referencedCommentId);
}
