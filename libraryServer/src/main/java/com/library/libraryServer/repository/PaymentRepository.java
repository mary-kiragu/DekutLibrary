package com.library.libraryServer.repository;

import com.library.libraryServer.domain.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findOneByTransactionCode(String transactionCode);

    Optional<Payment> findOneByMerchantRequestId(String merchantRequestId);



}
