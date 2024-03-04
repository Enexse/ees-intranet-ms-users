package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserCreateRequest;
import com.enexse.intranet.ms.users.models.EesUserRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EesCreateRequestRepository extends MongoRepository<EesUserCreateRequest,String> {
        List<EesUserCreateRequest> findAllByUserAndType(EesUser user, String type);

        @Query("{ createdAt: { $lte: ?0 }, type: 'MANUAL' }")
        List<EesUserCreateRequest> findLastRequestsAfterDate(String startOfDay, Pageable pageable);

}
