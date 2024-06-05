package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesMessageAlert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesMessageAlertRepository extends MongoRepository<EesMessageAlert,String> {
}
