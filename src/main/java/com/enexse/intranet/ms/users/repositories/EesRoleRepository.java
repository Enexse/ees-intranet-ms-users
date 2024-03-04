package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserPermission;
import com.enexse.intranet.ms.users.models.EesUserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EesRoleRepository extends MongoRepository<EesUserRole, String> {
    @Query("{ 'roleCode': ?0 }")
    EesUserRole findByCode(String roleCode);

    @Query("{ 'roleTitle': ?0 }")
    EesUserRole findByroleTitle(String roleTitle);

    EesUserRole findByPermissionsContains(EesUserPermission permission);


}
