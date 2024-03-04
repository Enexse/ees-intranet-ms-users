package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesOrderNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesOrderNumberRepository extends MongoRepository<EesOrderNumber, String> {

    EesOrderNumber findByCode(String code);

    boolean existsByCode(String ode);

    void deleteByCode(String code);
}
