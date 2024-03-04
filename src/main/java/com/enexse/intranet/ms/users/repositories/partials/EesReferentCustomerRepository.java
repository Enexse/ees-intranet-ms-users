package com.enexse.intranet.ms.users.repositories.partials;

import com.enexse.intranet.ms.users.models.partials.EesReferentCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesReferentCustomerRepository extends MongoRepository<EesReferentCustomer, String> {
    EesReferentCustomer findByReferentId(String referentId);
}
