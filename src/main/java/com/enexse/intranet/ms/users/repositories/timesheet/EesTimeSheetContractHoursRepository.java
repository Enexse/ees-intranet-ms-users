package com.enexse.intranet.ms.users.repositories.timesheet;

import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetActivity;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetContractHours;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface EesTimeSheetContractHoursRepository extends MongoRepository<EesTimeSheetContractHours, String> {

    @Query("{'contractHoursCode': ?0}")
    EesTimeSheetContractHours findByContractHoursCode(String contractHoursCode);

    boolean existsByContractHoursCode(String contractHoursCode);

    @Query("{'contractHours': ?0}")
    Optional<EesTimeSheetContractHours> findByContractHours(String contractHours);
}
