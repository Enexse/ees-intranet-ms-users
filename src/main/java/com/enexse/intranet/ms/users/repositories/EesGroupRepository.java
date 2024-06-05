package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesGroup;
import com.enexse.intranet.ms.users.models.EesUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EesGroupRepository extends MongoRepository<EesGroup, String> {
    @Query("{ 'groupCode': ?0 }")
    Optional<EesGroup> findByCode(String groupCode);

    @Query("{ 'groupCode': ?0, 'createdBy': ?1 }")
    Optional<EesGroup> findByGroupCodeAndCreatedBy(@Param("groupCode") String groupCode, @Param("createdBy") EesUser createdBy);
}
