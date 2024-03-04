package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesFormation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EesFormationRepository extends MongoRepository<EesFormation,String> {

}
