package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesCustomerRepository extends MongoRepository<EesCustomer, String> {

    EesCustomer findByCustomerCode(String customerCode);
    EesCustomer findByCustomerId (String customerId);
    boolean existsByCustomerCode(String customerCode);
}
