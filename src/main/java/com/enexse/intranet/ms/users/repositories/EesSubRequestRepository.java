package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserRequest;
import com.enexse.intranet.ms.users.models.EesUserSubRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EesSubRequestRepository extends MongoRepository<EesUserSubRequest, String> {
    @Query("{ 'eesUserRequest': ?0 }")
    List<EesUserSubRequest> findByRequest(EesUserRequest eesUserRequest);

    @Query("{'subRequestCode': ?0}")
    Optional<EesUserSubRequest> findByCode(String subRequestCode);

    @Query("{'subRequestId': ?0}")
    Optional<EesUserSubRequest> findBySubRequestId(String subRequestId);

    @Query("{'description': ?0}")
    EesUserSubRequest findByDescription(String description);
}
