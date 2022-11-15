package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.jpa.repository.*;

public interface BorrowHistoryRepository  extends JpaRepository<BorrowHistory,Long> {
}
