package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserRolePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EesUserAccessRepository extends MongoRepository<EesUserRolePermission,String> {

    @Query("{ 'roleCode': ?0 }")
    EesUserRolePermission findByRoleCode(String roleCode);
}
