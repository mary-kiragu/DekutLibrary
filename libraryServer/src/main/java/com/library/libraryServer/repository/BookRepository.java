package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.jpa.repository.*;

public interface BookRepository extends JpaRepository<Book,Long> {
Book findBookByTitle(String title);

}
