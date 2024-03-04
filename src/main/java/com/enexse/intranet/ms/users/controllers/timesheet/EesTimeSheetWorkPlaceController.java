package com.enexse.intranet.ms.users.controllers.timesheet;


import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetWorkplace;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesTimeSheetWorkPlaceRequest;
import com.enexse.intranet.ms.users.services.timesheet.EesTimeSheetWorkPlaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesTimeSheetWorkPlaceController {

    private EesTimeSheetWorkPlaceService timeSheetWorkPlaceService;

   /* @PostMapping(EesUserEndpoints.EES_INSERT_TIMESHEET_WORKPLACE)
    public ResponseEntity<Object> eesInsertTimeSheetWorkPlace(@Valid @RequestBody EesTimeSheetWorkPlaceRequest request) {
        return timeSheetWorkPlaceService.insertTimeSheetWorkPlace(request);
    }*/

    @GetMapping(EesUserEndpoints.EES_GET_All_TIMESHEET_WORKPLACES)
    public List<EesTimeSheetWorkplace> eesGetAllTimeSheetWorkPlaces() {
        return timeSheetWorkPlaceService.getAllTimeSheetWorkPlaces();
    }

    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_TIMESHEET_WORKPLACE)
    public ResponseEntity<Object> eesUpdateTimeSheetWorkPlaceByCode(@PathVariable String workPlaceCode, @RequestBody EesTimeSheetWorkPlaceRequest request) {
        return timeSheetWorkPlaceService.updateTimeSheetWorkPlaceByCode(workPlaceCode, request);
    }

    @GetMapping(EesUserEndpoints.EES_GET_TIMESHEET_WORKPLACE_BY_CODE)
    public EesTimeSheetWorkplace eesGetTimeSheetWorkPlaceByCode(@RequestParam String workPlaceCode) {
        return timeSheetWorkPlaceService.getTimeSheetWorkPlaceByCode(workPlaceCode);
    }

    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_TIMESHEET_WORKPLACE)
    public ResponseEntity<Object> eesDeleteTimeSheetWorkPlaceByCode(@PathVariable String workPlaceCode) {
        return timeSheetWorkPlaceService.deleteTimeSheetWorkPlaceByCode(workPlaceCode);
    }
}
