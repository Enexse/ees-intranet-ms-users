package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserDepartment;
import com.enexse.intranet.ms.users.models.EesUserEntity;
import com.enexse.intranet.ms.users.payload.request.EesEntityRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesUserEntityRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EesUserEntityService {

    private EesUserEntityRepository eesUserEntityRepository;
    private EesUserRepository eesUserRepository;

    public ResponseEntity<Object> insertEntity(EesEntityRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!request.getEntityCode().startsWith(EesUserResponse.EES_ENTITY_PREFIX) || request.getEntityCode().length() != 14) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ENTITY_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        EesUserEntity existingEntity = eesUserEntityRepository.findByEntityCode(request.getEntityCode().toUpperCase(Locale.ROOT));

        if (existingEntity != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ENTITY_ALREADY_EXISTS, request.getEntityCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesUserEntity entity = new EesUserEntity()
                    .builder()
                    .entityCode(request.getEntityCode().toUpperCase(Locale.ROOT))
                    .entityDescription(EesCommonUtil.generateCapitalize(request.getEntityDescription()))
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesUserEntityRepository.save(entity);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ENTITY_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesUserEntity> getAllEntities() {
        List<EesUserEntity> entities = eesUserEntityRepository
                .findAll()
                .stream().sorted(Comparator.comparing(EesUserEntity::getCreatedAt).reversed()).collect(Collectors.toList());
        return entities;
    }

    public EesUserEntity getEntityByCode(String entityCode) {
        if (!entityCode.startsWith(EesUserResponse.EES_ENTITY_PREFIX)) {
            entityCode = EesUserResponse.EES_ENTITY_PREFIX + entityCode;
        }
        return eesUserEntityRepository.findByEntityCode(entityCode);
    }

    public Optional<List<EesUser>> getUsersEntity(String entityCode) {
        EesUserEntity eesEntity = eesUserEntityRepository.findByEntityCode(EesUserResponse.EES_ENTITY_PREFIX + entityCode);
        System.out.println("***" + eesEntity);
        return Optional.ofNullable(eesUserRepository.findByEesEntity(Optional.ofNullable(eesEntity)));
    }

    public ResponseEntity<Object> updateEntityByCode(String entityCode, EesEntityRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String prefixEntityCode = EesUserResponse.EES_ENTITY_PREFIX + entityCode;
        EesUserEntity entity = eesUserEntityRepository.findByEntityCode(prefixEntityCode);
        if (entity == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ENTITY_NOT_FOUND + entityCode), HttpStatus.NOT_FOUND);
        } else {
            if (request.getEntityCode().length() != 14) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ENTITY_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                //added an extra check before checking for the duplicate entity code.
                if (!request.getEntityCode().equals(prefixEntityCode)) {
                    EesUserEntity existingEntity = eesUserEntityRepository.findByEntityCode(request.getEntityCode().toUpperCase(Locale.ROOT));
                    if (existingEntity != null) {
                        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ENTITY_ALREADY_EXISTS, request.getEntityCode())), HttpStatus.BAD_REQUEST);
                    }
                }
                // Check the duplicate description before saving
                EesUserEntity existingEntity = eesUserEntityRepository.findByEntityDescription(EesCommonUtil.generateCapitalize(request.getEntityDescription()));
                if (existingEntity != null) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_ENTITY_ALREADY_EXISTS_DESCRIPTION, request.getEntityDescription())), HttpStatus.BAD_REQUEST);
                }
                entity.setEntityCode(request.getEntityCode());
                entity.setEntityDescription(EesCommonUtil.generateCapitalize(request.getEntityDescription()));
                entity.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                entity.setCreatedBy(user.get());
                eesUserEntityRepository.save(entity);
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_ENTITY), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> deleteEntityByCode(String entityCode) {

        EesUserEntity entity = eesUserEntityRepository.findByEntityCode(EesUserResponse.EES_ENTITY_PREFIX + entityCode);
        if (entity == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ENTITY_NOT_FOUND + entityCode), HttpStatus.NOT_FOUND);
        } else {

            Optional<List<EesUser>> entityUsers = Optional.ofNullable(eesUserRepository.findByEesEntity(Optional.of(entity)));
            List<EesUser> users = entityUsers.get();

            for (EesUser user : entityUsers.get()) {
                user.setEesEntity(null);
                eesUserRepository.save(user);
            }
            eesUserEntityRepository.delete(entity);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_ENTITY_DELETED), HttpStatus.OK);
        }
    }
}
