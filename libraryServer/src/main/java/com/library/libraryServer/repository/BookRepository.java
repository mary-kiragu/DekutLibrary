package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface BookRepository extends JpaRepository<Book,Long> {
Book findBookByTitle(String title);

    List<Book> findByCategoryId(Integer categoryId);

    List<Book> findByBorrowedBy(String borrowedBy);

    List<Book> findByTitleContainingOrAuthorContaining(@Param("text") String text, @Param("text1")String text1);
}
