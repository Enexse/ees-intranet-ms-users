package com.enexse.intranet.ms.users.openfeign;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetWorkplace;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(name = EesUserConstants.EES_APP_NAME_ACCOUNTING, configuration = EesTimeSheetWorkplace.class)
public interface EesWorkplaceService {

    @GetMapping(EesUserEndpoints.EES_ACCOUNTING_ENDPOINT + EesUserEndpoints.EES_GET_TIMESHEET_WORKPLACE_BY_CODE)
    EesTimeSheetWorkplace findByCode(@RequestParam String workPlaceCode);
}
