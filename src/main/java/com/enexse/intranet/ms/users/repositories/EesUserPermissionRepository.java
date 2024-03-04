package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserPermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EesUserPermissionRepository extends MongoRepository<EesUserPermission , String> {

    //Spring Data MongoDB GENERATE THIS METHOD BASED ON IT NAME
    boolean existsByPermissionCode(String permissionCode);

    EesUserPermission findByPermissionCode(String permissionCode);

}
