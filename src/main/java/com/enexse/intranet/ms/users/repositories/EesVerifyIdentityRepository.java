package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesVerifyIdentity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EesVerifyIdentityRepository extends MongoRepository<EesVerifyIdentity, String> {
    @Query("{'userId': ?0}")
    Optional<List<EesVerifyIdentity>> findByUserId(String userId);

    Optional<EesVerifyIdentity> findByLinkAndVerifyType(String link, String verifyType);

    @Query("{'link': ?0}")
    Optional<EesVerifyIdentity> findByLink(String link);

    Optional<EesVerifyIdentity> findByUserEmailAndCode(String userEmail, String code);

    Optional<EesVerifyIdentity> findByUserEmailAndVerifyType(String userEmail, String verifyType);
}
