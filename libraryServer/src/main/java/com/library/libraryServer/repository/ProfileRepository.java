package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.jpa.repository.*;

public interface ProfileRepository  extends JpaRepository<Profile,Long> {
    Iterable<Profile> findAllByAccount(Long userId);
    Long countByAccount(Long account);
}
