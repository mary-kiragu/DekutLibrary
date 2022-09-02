package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> findByParentCategoryId(Integer parentCategoryId);
}
