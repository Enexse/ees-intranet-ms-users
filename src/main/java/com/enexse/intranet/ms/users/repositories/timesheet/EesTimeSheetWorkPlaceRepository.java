package com.enexse.intranet.ms.users.repositories.timesheet;

import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetWorkplace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EesTimeSheetWorkPlaceRepository extends MongoRepository<EesTimeSheetWorkplace, String> {

    @Query("{'workPlaceCode': ?0}")
    EesTimeSheetWorkplace findByWorkPlaceCode(String workPlaceCode);

    boolean existsByWorkPlaceCode(String workPlaceCode);

}
