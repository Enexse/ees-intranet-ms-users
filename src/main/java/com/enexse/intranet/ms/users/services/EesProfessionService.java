package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesMessageAlert;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserDepartment;
import com.enexse.intranet.ms.users.models.EesUserProfession;
import com.enexse.intranet.ms.users.payload.request.EesProfessionRequest;
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
public class EesProfessionService {

    private EesUserProfessionRepository professionRepository;

    private EesUserDepartmentRepository departmentRepository;
    private EesUserRepository eesUserRepository;

    public ResponseEntity<Object> insertProfession(EesProfessionRequest professionRequest) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(professionRequest.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, professionRequest.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String professionCode = professionRequest.getProfessionCode();

        // Check if professionCode is null or empty
        if (professionCode == null || professionCode.trim().isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PROFESSION_CODE_COULD_NOT_BE_NULL), HttpStatus.BAD_REQUEST);
        }

        // Check if professionCode starts with "EES-PERMISSION-"
        if (!professionCode.startsWith(EesUserResponse.EES_PROFESSION_PREFIX)) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PROFESSION_CODE_INVALID_FORMAT), HttpStatus.BAD_REQUEST);
        }

        // Check if permissionCode is already used
        if (professionRepository.existsByProfessionCode(professionCode)) {

            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_PROFESSION_ALREADY_EXISTS, professionRequest.getProfessionCode())), HttpStatus.BAD_REQUEST);
        }
        //Check if departement exists
        EesUserDepartment userDepartment = departmentRepository.findByCompanyDepartmentCode(professionRequest.getDepartmentCode());
        if (userDepartment == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_DEPARTMENT_NOT_FOUND, professionRequest.getDepartmentCode())), HttpStatus.BAD_REQUEST);
        }

        EesUserProfession userProfession = EesUserProfession
                .builder()
                .professionCode(professionRequest.getProfessionCode())
                .professionName(professionRequest.getProfessionName())
                .departmentCode(userDepartment.getDepartmentCode())
                .departmentDescription(userDepartment.getDepartmentDescription())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .createdBy(user.get())
                .build();
        professionRepository.save(userProfession);
        userDepartment.getProfessions().add(userProfession.getProfessionCode());
        departmentRepository.save(userDepartment);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PROFESSION_CREATED), HttpStatus.CREATED);

    }


    public ResponseEntity<Object> getAllProfessionsByDepartment(String departmentCode) {
        if (departmentCode.isEmpty()) {
            return new ResponseEntity<Object>(null, HttpStatus.OK);
        }
        EesUserDepartment userDepartment = departmentRepository.findByCompanyDepartmentCode(departmentCode);
        if (userDepartment == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_NOT_FOUND, departmentCode)), HttpStatus.BAD_REQUEST);
        } else {
            List<EesUserProfession> professions = new ArrayList<>();
            for (String code : userDepartment.getProfessions()) {
                EesUserProfession profession = professionRepository.findByProfessionCode(code);
                professions.add(profession);
            }
            return new ResponseEntity<Object>(professions, HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> updateProfession(String professionCode, EesProfessionRequest professionRequest) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(professionRequest.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, professionRequest.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!professionCode.startsWith(EesUserResponse.EES_PROFESSION_PREFIX)) {
            professionCode = EesUserResponse.EES_PROFESSION_PREFIX + professionCode;
        }
        EesUserProfession profession = professionRepository.findByProfessionCode(professionCode);
        if (profession == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PROFESSION_NOT_FOUND + "" + professionCode), HttpStatus.NOT_FOUND);
        } else {
            if (!professionRequest.getProfessionCode().equals(professionCode)) {
                EesUserProfession existingProfession = professionRepository.findByProfessionCode(professionRequest.getProfessionCode().toUpperCase(Locale.ROOT));
                if (existingProfession != null) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_PROFESSION_ALREADY_EXISTS, professionRequest.getProfessionCode())), HttpStatus.BAD_REQUEST);
                }
            }
        }

        // Check if departement exists
        EesUserDepartment userDepartment = departmentRepository.findByCompanyDepartmentCode(professionRequest.getDepartmentCode());
        if (userDepartment == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_DEPARTMENT_NOT_FOUND, professionRequest.getDepartmentCode())), HttpStatus.BAD_REQUEST);
        }

        profession.setProfessionCode(professionRequest.getProfessionCode());
        profession.setProfessionName(professionRequest.getProfessionName());
        profession.setDepartmentCode(userDepartment.getDepartmentCode());
        profession.setDepartmentDescription(userDepartment.getDepartmentDescription());
        profession.setCreatedBy(user.get());
        profession.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
        professionRepository.save(profession);
        userDepartment.getProfessions().add(profession.getProfessionCode());
        System.out.println("***" + userDepartment.getProfessions());
        departmentRepository.save(userDepartment);

        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PROFESSION_UPDATED), HttpStatus.OK);
    }

    public List<EesUserProfession> getAllProfessions() {
        List<EesUserProfession> professions = professionRepository.findAll()
                .stream().sorted(Comparator.comparing(EesUserProfession::getCreatedAt).reversed()).collect(Collectors.toList());
        return professions;
    }

    public EesUserProfession getProfessionByCode(String professionCode) {
        if (!professionCode.startsWith(EesUserResponse.EES_PROFESSION_PREFIX)) {
            professionCode = EesUserResponse.EES_PROFESSION_PREFIX + professionCode;
        }
        if ((professionRepository.findByProfessionCode(professionCode)) == null) {
            return null;
        } else {
            return professionRepository.findByProfessionCode(professionCode);
        }
    }

    public ResponseEntity<Object> deleteProfessionByCode(String professionCode) {
        EesUserProfession profession = professionRepository.findByProfessionCode(professionCode);

        if (profession == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PROFESSION_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        EesUserDepartment department = departmentRepository.findByCompanyDepartmentCode(profession.getDepartmentCode());

        // Remove profession from department's array of professions
        department.getProfessions().removeIf(p -> p.equals(professionCode));
        departmentRepository.save(department);

        // Delete profession from database
        professionRepository.delete(profession);

        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PROFESSION_DELETED), HttpStatus.OK);
    }
}
