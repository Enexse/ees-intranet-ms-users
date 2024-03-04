package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesContract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesContractRepository extends MongoRepository<EesContract, String>  {

    EesContract findByContractCode(String contractCode);
    boolean existsByContractCode(String contractCode);
}
