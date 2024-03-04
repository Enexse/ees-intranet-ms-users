package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesGroup;
import com.enexse.intranet.ms.users.models.EesUserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EesGroupRepository extends MongoRepository<EesGroup,String> {
    @Query("{ 'groupCode': ?0 }")
    Optional<EesGroup> findByCode(String groupCode);
}
