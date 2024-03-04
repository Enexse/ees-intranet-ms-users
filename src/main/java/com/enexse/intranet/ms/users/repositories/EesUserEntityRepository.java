package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EesUserEntityRepository extends MongoRepository<EesUserEntity,String> {

    @Query("{'entityCode' :?0}")
    EesUserEntity findByEntityCode(String entityCode);

    @Query("{'entityDescription' : ?0}")
    EesUserEntity findByEntityDescription(String entityDescription);
}
