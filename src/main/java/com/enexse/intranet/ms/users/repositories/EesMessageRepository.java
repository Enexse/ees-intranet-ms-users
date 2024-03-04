package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesMessageRepository extends MongoRepository<EesMessage, String> {
}
