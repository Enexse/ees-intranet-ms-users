package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesCaseNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesCaseNumberRepository extends MongoRepository<EesCaseNumber, String> {

    EesCaseNumber findByCode(String code);

    boolean existsByCode(String ode);

    void deleteByCode(String code);
}
