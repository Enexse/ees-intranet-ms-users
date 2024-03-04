package com.enexse.intranet.ms.users.repositories.timesheet;

import com.enexse.intranet.ms.users.models.timesheet.EesUserTimesheet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EesUserTimesheetRepository extends MongoRepository<EesUserTimesheet,String> {

}
