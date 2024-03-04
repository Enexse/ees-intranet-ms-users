package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.*;
import com.enexse.intranet.ms.users.payload.request.EesDepartmentRequest;
import com.enexse.intranet.ms.users.payload.request.EesGroupRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesGroupRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EesGroupService {

    private EesGroupRepository eesGroupRepository;
    private EesUserRepository eesUserRepository ;

    public ResponseEntity<Object> insertGroup(EesGroupRequest request) {
        Optional<EesUser> user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!request.getGroupCode().startsWith(EesUserResponse.EES_GROUP_PREFIX) || request.getGroupCode().length() != 13) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_GROUP_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        Optional<EesGroup> existingGroup = eesGroupRepository.findByCode(request.getGroupCode().toUpperCase(Locale.ROOT));

        if (existingGroup.isPresent()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_GROUP_ALREADY_EXISTS, request.getGroupCode())), HttpStatus.BAD_REQUEST);
        }

        List<EesUser> collaborators = new ArrayList<>();
        for (String collaboratorId : request.getCollaboratorsIds()) {
            Optional<EesUser> collaborator = eesUserRepository.findById(collaboratorId);
            collaborator.ifPresent(collaborators::add);
        }

        EesGroup group = EesGroup.builder()
                .groupCode(request.getGroupCode())
                .groupTitle(request.getGroupTitle())
                .groupDescription(request.getGroupDescription())
                .collaborators(collaborators)
                .groupTimesheet(request.isGroupTimesheet())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .createdBy(user.get())
                .build();

        eesGroupRepository.save(group);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_GROUP_CREATED), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getAllGroups() {
        List<EesGroup> list = null;
        list = eesGroupRepository.findAll();
        return ResponseEntity.ok(list);
    }

    public Optional<EesGroup> getGroupByCode(String groupCode){
        if(!groupCode.startsWith(EesUserResponse.EES_GROUP_PREFIX)){
            groupCode = EesUserResponse.EES_GROUP_PREFIX + groupCode;
        }
        return eesGroupRepository.findByCode(groupCode);
    }

    public ResponseEntity<Object> updateGroupByCode(String groupCode, EesGroupRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if(user.isEmpty()){
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if(!groupCode.startsWith(EesUserResponse.EES_GROUP_PREFIX)){
            groupCode = EesUserResponse.EES_GROUP_PREFIX +  groupCode;
        }
        Optional<EesGroup> group = eesGroupRepository.findByCode(groupCode);
        if (group.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_GROUP_NOT_FOUND + groupCode), HttpStatus.NOT_FOUND);
        } else {
            if (request.getGroupCode().length() != 13) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_GROUP_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {

                List<EesUser> collaborators = new ArrayList<>();
                for (String collaboratorId : request.getCollaboratorsIds()) {
                    Optional<EesUser> collaborator = eesUserRepository.findById(collaboratorId);
                    collaborator.ifPresent(collaborators::add);
                }

                group.get().setGroupCode(request.getGroupCode());
                group.get().setGroupTitle(request.getGroupTitle());
                group.get().setGroupDescription(request.getGroupDescription());
                group.get().setCollaborators(collaborators);
                group.get().setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                group.get().setCreatedBy(user.get());
                group.get().setGroupTimesheet(request.isGroupTimesheet());
                eesGroupRepository.save(group.get());
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_GROUP), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> deleteGroup(String groupCode){
        if (!groupCode.startsWith(EesUserResponse.EES_GROUP_PREFIX)) {
            groupCode = EesUserResponse.EES_GROUP_PREFIX + groupCode;
        }

        Optional<EesGroup> group = eesGroupRepository.findByCode(groupCode);
        if(group.isEmpty()){
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_GROUP_NOT_FOUND), HttpStatus.NOT_FOUND);
        }else{
            eesGroupRepository.delete(group.get());
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_GROUP_DELETED), HttpStatus.OK);
        }
    }
}
