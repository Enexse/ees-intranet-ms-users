package com.enexse.intranet.ms.users.repositories;

import com.enexse.intranet.ms.users.models.EesUserProfession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EesUserProfessionRepository  extends MongoRepository <EesUserProfession,String> {

    boolean existsByProfessionCode(String professionCode);

    @Query("{'professionCode' :?0}")
    EesUserProfession findByProfessionCode(String professionCode);

    @Query("{'professionName' :?0}")
    EesUserProfession findByProfessionName(String professionName);
}
