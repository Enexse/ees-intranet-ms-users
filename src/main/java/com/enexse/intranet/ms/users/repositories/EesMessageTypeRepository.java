package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesMessageType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesMessageTypeRepository extends MongoRepository<EesMessageType, String> {

    EesMessageType findByCode(String code);
}
