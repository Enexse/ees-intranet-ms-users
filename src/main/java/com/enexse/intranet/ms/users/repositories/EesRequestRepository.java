package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserRequest;
import com.enexse.intranet.ms.users.models.EesUserSubRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EesRequestRepository extends MongoRepository<EesUserRequest,String> {
    @Query("{ 'requestCode': ?0 }")
    EesUserRequest findByCode(String requestCode);

    @Query("{ 'requestTitle': ?0 }")
    EesUserRequest findByTitle(String requestTitle);

    @Query("{'requestId': ?0}")
    Optional<EesUserRequest> findByRequestId(String requestId);
}
