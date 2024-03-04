package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesMessage;
import com.enexse.intranet.ms.users.models.EesMessageAlert;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EesMessageAlertRepository extends MongoRepository<EesMessageAlert,String> {
}
