package com.enexse.intranet.ms.users.repositories.partials;

import com.enexse.intranet.ms.users.models.partials.EesUserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EesUserInfoRepository extends MongoRepository<EesUserInfo, String> {

    EesUserInfo findByCollaboratorId(String userId);

    EesUserInfo findByUserId(String userId);

    @Query("{ $expr: { $and: [ { $ne: [ '$passwordChangedAt', null ] }, { $lt: [ '$passwordChangedAt', { $dateAdd: { startDate: '$$NOW', unit: 'minute', amount: -20 } } ] } ] } }")
    Optional<List<EesUserInfo>> findUsersWithExpiredPasswords(@Param("NOW") Date now);

}
