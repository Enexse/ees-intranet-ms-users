package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserDepartment;
import com.enexse.intranet.ms.users.models.EesUserProfession;
import com.enexse.intranet.ms.users.payload.request.EesDepartmentRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesUserDepartmentRepository;
import com.enexse.intranet.ms.users.repositories.EesUserProfessionRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EesUserDepartmentService {

    private EesUserDepartmentRepository eesUserDepartmentRepository;
    private EesUserProfessionRepository professionRepository;
    private EesUserRepository eesUserRepository;

    public ResponseEntity<Object> insertDepartment(EesDepartmentRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!request.getDepartmentCode().startsWith(EesUserResponse.EES_DEPARTMENT_PREFIX) || request.getDepartmentCode().length() != 18) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_DEPARTMENT_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        EesUserDepartment existingDepartment = eesUserDepartmentRepository.findByCompanyDepartmentCode(request.getDepartmentCode().toUpperCase(Locale.ROOT));

        if (existingDepartment != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_DEPARTMENT_ALREADY_EXISTS, request.getDepartmentCode())), HttpStatus.BAD_REQUEST);
        } else {
            List<String> professions = new ArrayList<>();
            EesUserDepartment department = new EesUserDepartment()
                    .builder()
                    .departmentCode(request.getDepartmentCode().toUpperCase(Locale.ROOT))
                    .departmentDescription(request.getDepartmentDescription())
                    .professions(professions)
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .defaultColor(EesCommonUtil.generateDefaultDepartmentColor())
                    .createdBy(user.get())
                    .build();
            eesUserDepartmentRepository.save(department);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_DEPARTMENT_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesUserDepartment> getAllDepartments() {
        List<EesUserDepartment> departments = eesUserDepartmentRepository
                .findAll()
                .stream().sorted(Comparator.comparing(EesUserDepartment::getCreatedAt).reversed()).collect(Collectors.toList());
        return departments;
    }

    public Optional<List<EesUser>> getUsersByDepartment(String departmentCode) {
        //Optional<EesUserDepartment> eesDepartment = eesUserDepartmentRepository.findById(departmentId);
        EesUserDepartment eesDepartment = eesUserDepartmentRepository.findByCompanyDepartmentCode(EesUserResponse.EES_DEPARTMENT_PREFIX + departmentCode);
        System.out.println(eesDepartment);
        return Optional.ofNullable(eesUserRepository.findByEesDepartment(Optional.ofNullable(eesDepartment)));
    }

    public EesUserDepartment getDepartmentByCode(String departmentCode) {
        if (!departmentCode.startsWith(EesUserResponse.EES_DEPARTMENT_PREFIX)) {
            departmentCode = EesUserResponse.EES_DEPARTMENT_PREFIX + departmentCode;
        }
        return eesUserDepartmentRepository.findByCompanyDepartmentCode(departmentCode);
    }

    public ResponseEntity<Object> updateDepartmentByCode(String departmentCode, EesDepartmentRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String prefixEntityCode = EesUserResponse.EES_DEPARTMENT_PREFIX + departmentCode;
        EesUserDepartment department = eesUserDepartmentRepository.findByCompanyDepartmentCode(prefixEntityCode);
        if (department == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ENTITY_NOT_FOUND + departmentCode), HttpStatus.NOT_FOUND);
        } else {
            if (request.getDepartmentCode().length() != 18) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_DEPARTMENT_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                //added an extra check before checking for the duplicate department code.
                if (!request.getDepartmentCode().equals(prefixEntityCode)) {
                    EesUserDepartment existingDepartment = eesUserDepartmentRepository.findByCompanyDepartmentCode(request.getDepartmentCode().toUpperCase(Locale.ROOT));
                    if (existingDepartment != null) {
                        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_DEPARTMENT_ALREADY_EXISTS, request.getDepartmentCode())), HttpStatus.BAD_REQUEST);
                    }
                }
                department.setDepartmentCode(request.getDepartmentCode());
                department.setDepartmentDescription(request.getDepartmentDescription());
                department.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                department.setCreatedBy(user.get());
                eesUserDepartmentRepository.save(department);
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_DEPARTMENT), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> deleteDepartmentByCode(String departmentCode) {

        EesUserDepartment eesDepartment = eesUserDepartmentRepository.findByCompanyDepartmentCode(EesUserResponse.EES_DEPARTMENT_PREFIX + departmentCode);
        if (eesDepartment == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_DEPARTMENT_NOT_FOUND + departmentCode), HttpStatus.NOT_FOUND);
        } else {

            Optional<List<EesUser>> departmentUsers = Optional.ofNullable(eesUserRepository.findByEesDepartment(Optional.of(eesDepartment)));
            List<EesUser> users = departmentUsers.get();

            for (EesUser user : departmentUsers.get()) {
                // Mettre à jour le statut du collaborateur
                user.setEesEntity(null);
                // Enregistrer les modifications du collaborateur dans la base de données
                eesUserRepository.save(user);
            }
            //delete department
            eesUserDepartmentRepository.delete(eesDepartment);
            //delete professions in the department
            List<String> professions = eesDepartment.getProfessions();
            for (String code : professions) {
                EesUserProfession profession = professionRepository.findByProfessionCode(code);
                professionRepository.delete(profession);
            }
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_DEPARTMENT_DELETED), HttpStatus.OK);
        }
    }
}
