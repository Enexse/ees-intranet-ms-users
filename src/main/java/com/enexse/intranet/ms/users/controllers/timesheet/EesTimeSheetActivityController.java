package com.enexse.intranet.ms.users.controllers.timesheet;

import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetActivity;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesTimeSheetActivityRequest;
import com.enexse.intranet.ms.users.services.timesheet.EesTimeSheetActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesTimeSheetActivityController {
    private EesTimeSheetActivityService timeSheetActivityService;

    @PostMapping(EesUserEndpoints.EES_INSERT_TIMESHEET_ACTIVITY)
    public ResponseEntity<Object> eesInsertTimeSheetActivity(@Valid @RequestBody EesTimeSheetActivityRequest request) {
        return timeSheetActivityService.insertTimeSheetActivity(request);
    }

    @GetMapping(EesUserEndpoints.EES_GET_All_TIMESHEET_ACTIVITIES)
    public List<EesTimeSheetActivity> eesGetAllTimeSheetActivities() {
        return timeSheetActivityService.getAllTimeSheetActivities();
    }

    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_TIMESHEET_ACTIVITY)
    public ResponseEntity<Object> eesUpdateTimeSheetActivityByCode(@PathVariable String activityCode, @RequestBody EesTimeSheetActivityRequest request) {
        return timeSheetActivityService.updateTimeSheetActivityByCode(activityCode, request);
    }

    @GetMapping(EesUserEndpoints.EES_GET_TIMESHEET_ACTIVITY_BY_CODE)
    public EesTimeSheetActivity eesGetTimeSheetActivityByCode(@RequestParam String activityCode) {
        return timeSheetActivityService.getTimeSheetActivityByCode(activityCode);
    }

    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_TIMESHEET_ACTIVITY)
    public ResponseEntity<Object> eesDeleteTimeSheetActivityByCode(@PathVariable String activityCode) {
        return timeSheetActivityService.deleteTimeSheetActivityByCode(activityCode);
    }
}
