package com.enexse.intranet.ms.users.services.timesheet;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.*;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetActivity;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetContractHours;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetWorkplace;
import com.enexse.intranet.ms.users.models.timesheet.EesUserTimesheet;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesUserTimesheetRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.*;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetActivityRepository;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetContractHoursRepository;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetWorkPlaceRepository;
import com.enexse.intranet.ms.users.repositories.timesheet.EesUserTimesheetRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EesUserTimesheetService {
    private EesUserTimesheetRepository eesUserTimesheetRepository;
    private EesUserRepository eesUserRepository;
    private EesCustomerRepository eesCustomerRepository;
    private EesTimeSheetActivityRepository eesTimeSheetActivityRepository;
    private EesTimeSheetWorkPlaceRepository eesTimeSheetWorkPlaceRepository;
    private EesTimeSheetContractHoursRepository eesTimeSheetContractHoursRepository;
    private EesUserEntityRepository eesUserEntityRepository;
    private EesUserDepartmentRepository eesUserDepartmentRepository;
    private EesUserProfessionRepository eesUserProfessionRepository;

    public ResponseEntity<Object> updateUser(String userId, EesUserTimesheetRequest request){
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if(user.isPresent()){
            EesCustomer customer = eesCustomerRepository.findByCustomerCode(request.getCustomerCode());
            EesTimeSheetActivity activity = eesTimeSheetActivityRepository.findByActivityCode(request.getActivityCode());
            EesTimeSheetWorkplace workplace =  eesTimeSheetWorkPlaceRepository.findByWorkPlaceCode(request.getWorkplaceCode());
            EesTimeSheetContractHours contractHours = eesTimeSheetContractHoursRepository.findByContractHoursCode(request.getContractHoursCode());

            if(request.getEntityCode() != null){
                EesUserEntity entity = eesUserEntityRepository.findByEntityCode(request.getEntityCode());
                user.get().setEesEntity(entity);
            }
            if(request.getDepartmentCode() != null){
                EesUserDepartment department = eesUserDepartmentRepository.findByCompanyDepartmentCode(request.getDepartmentCode());
                user.get().setEesDepartment(department);
                EesUserProfession profession = eesUserProfessionRepository.findByProfessionCode(request.getProfessionCode());
                user.get().setEesProfession(profession);
            }

            EesUserTimesheet userTimesheet = new EesUserTimesheet()
                    .builder()
                    .user(user.get())
                    .businessManagerType(request.getBusinessManagerType())
                    .businessManagerEnexse(request.getBusinessManagerEnexse())
                    .businessManagerClient(request.getBusinessManagerClient())
                    .rtt(request.getRtt())
                    .eesCustomer(customer)
                    .numAffair(request.getNumAffair())
                    .timeSheetActivity(activity)
                    .contractHours(contractHours)
                    .workplace(workplace)
                    .projectName(request.getProjectName())
                    .build();
            eesUserTimesheetRepository.save(userTimesheet);
            user.get().setEesUserTimesheet(userTimesheet);
            eesUserRepository.save(user.get());

        }else{
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }
}
