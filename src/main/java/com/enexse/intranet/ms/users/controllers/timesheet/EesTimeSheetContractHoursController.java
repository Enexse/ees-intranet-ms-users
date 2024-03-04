package com.enexse.intranet.ms.users.controllers.timesheet;

import com.enexse.intranet.ms.users.constants.EesUserEndpoints;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesTimeSheetContractHoursRequest;
import com.enexse.intranet.ms.users.services.timesheet.EesTimeSheetContractHoursService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(EesUserEndpoints.EES_ROOT_ENDPOINT)
@CrossOrigin("*")
@AllArgsConstructor
public class EesTimeSheetContractHoursController {

    private EesTimeSheetContractHoursService timeSheetContractHoursService;

    @PostMapping(EesUserEndpoints.EES_INSERT_TIMESHEET_CONTRACTHOURS)
    public ResponseEntity<Object> eesInsertTimeSheetContractHours(@Valid @RequestBody EesTimeSheetContractHoursRequest request) {
        return timeSheetContractHoursService.insertContractHours(request);
    }

    @GetMapping(EesUserEndpoints.EES_GET_All_TIMESHEET_CONTRACTHOURS)
    public List<com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetContractHours> eesGetAllTimeSheetActivities() {
        return timeSheetContractHoursService.getAllTimeSheetContractHours();
    }

    @PutMapping(EesUserEndpoints.EES_UPDATE_BY_CODE_TIMESHEET_CONTRACTHOURS)
    public ResponseEntity<Object> eesUpdateTimeSheetContractHoursByCode(@PathVariable String contractHoursCode, @RequestBody EesTimeSheetContractHoursRequest request) {
        return timeSheetContractHoursService.updateTimeSheetContractHoursByCode(contractHoursCode, request);
    }

    @GetMapping(EesUserEndpoints.EES_GET_TIMESHEET_CONTRACTHOURS_BY_CODE)
    public com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetContractHours eesGetTimeSheetContractHoursByCode(@PathVariable String contractHoursCode) {
        return timeSheetContractHoursService.getTimeSheetContractHoursByCode(contractHoursCode);
    }

    @DeleteMapping(EesUserEndpoints.EES_DELETE_BY_CODE_TIMESHEET_CONTRACTHOURS)
    public ResponseEntity<Object> eesDeleteTimeSheetContractHoursByCode(@PathVariable String contractHoursCode) {
        return timeSheetContractHoursService.deleteTimeSheetContractHoursByCode(contractHoursCode);
    }
}
