package com.enexse.intranet.ms.users.repositories.timesheet;

import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetActivity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EesTimeSheetActivityRepository extends MongoRepository<EesTimeSheetActivity, String> {

    @Query("{'activityCode': ?0}")
    EesTimeSheetActivity findByActivityCode(String activityCode);

    boolean existsByActivityCode(String activityCode);

}
