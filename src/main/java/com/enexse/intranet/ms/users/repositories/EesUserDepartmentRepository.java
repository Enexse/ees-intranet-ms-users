package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserDepartment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EesUserDepartmentRepository extends MongoRepository<EesUserDepartment,String> {

    @Query("{'departmentCode' :?0}")
    EesUserDepartment findByCompanyDepartmentCode(String departmentCode);

    @Query("{'departmentDescription' : ?0}")
    EesUserDepartment findByDepartmentDescription(String departmentDescription);
}
