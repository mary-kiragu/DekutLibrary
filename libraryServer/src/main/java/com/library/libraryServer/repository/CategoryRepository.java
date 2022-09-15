package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> findByParentCategoryId(Integer parentCategoryId);

    List<Category> findByNameContainingOrDescriptionContaining(@Param("text")String text,@Param("text1") String text1);
}
