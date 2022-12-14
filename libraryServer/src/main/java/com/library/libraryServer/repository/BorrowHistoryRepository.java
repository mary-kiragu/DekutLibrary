package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BorrowHistoryRepository  extends JpaRepository<BorrowHistory,Long> {
    List<HistoryDTO> findByUserId(Long userId);
}
