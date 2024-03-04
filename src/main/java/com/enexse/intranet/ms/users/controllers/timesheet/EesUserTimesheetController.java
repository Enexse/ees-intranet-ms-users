package com.enexse.intranet.ms.users.controllers.timesheet;

import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesUserTimesheetRequest;
import com.enexse.intranet.ms.users.services.timesheet.EesUserTimesheetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesUserTimesheetController {
    private EesUserTimesheetService eesUserTimesheetService;

    @PutMapping(EesUserEndpoints.EES_UPDATE_TIMESHEET_USER)
    public ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody EesUserTimesheetRequest request){
        return eesUserTimesheetService.updateUser(userId, request);
    }
}
