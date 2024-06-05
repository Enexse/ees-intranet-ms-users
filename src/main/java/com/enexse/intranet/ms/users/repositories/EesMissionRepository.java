package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesMission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesMissionRepository extends MongoRepository<EesMission,String> {
}
