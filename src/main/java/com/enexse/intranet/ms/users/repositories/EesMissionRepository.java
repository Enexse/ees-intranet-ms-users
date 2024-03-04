package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesGroup;
import com.enexse.intranet.ms.users.models.EesMission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EesMissionRepository extends MongoRepository<EesMission,String> {
}
