package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesMessage;
import com.enexse.intranet.ms.users.models.EesMessageType;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.payload.request.EesMessageRequest;
import com.enexse.intranet.ms.users.payload.request.EesMessageTypeRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesMessageRepository;
import com.enexse.intranet.ms.users.repositories.EesMessageTypeRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EesMessageService {

    private EesMessageRepository msgRepository;
    private EesUserRepository eesUserRepository;
    private EesMessageTypeRepository eesMessageTypeRepository;

    public ResponseEntity<Object> insertMessage(EesMessageRequest messageRequest) {
        System.out.println(messageRequest);
        EesMessage newMessage = EesMessage.builder()
                .firstName(messageRequest.getFirstName())
                .lastName(messageRequest.getLastName())
                .email(messageRequest.getEmail())
                .phone(messageRequest.getPhone())
                .country(messageRequest.getCountry())
                .defaultAvatar(EesCommonUtil.generateDefaultAvatar())
                .subject(messageRequest.getSubject())
                .message(messageRequest.getMessage())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .build();
        msgRepository.save(newMessage);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_CREATED), HttpStatus.CREATED);
    }

    public List<EesMessage> getAllMessages() {
        List<EesMessage> messages = msgRepository.findAll();
        return messages;
    }

    public ResponseEntity<Object> insertMessageType(EesMessageTypeRequest request) {
        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!request.getCode().startsWith(EesUserResponse.EES_MESSAGE_TYPE_PREFIX) || request.getCode().length() != 20) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_TYPE_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }

        EesMessageType existingMessageType = eesMessageTypeRepository.findByCode(request.getCode().toUpperCase(Locale.ROOT));

        if (existingMessageType != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_MESSAGE_TYPE_ALREADY_EXISTS, request.getCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesMessageType eesMessageType = new EesMessageType()
                    .builder()
                    .code(request.getCode().toUpperCase(Locale.ROOT))
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesMessageTypeRepository.save(eesMessageType);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_TYPE_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesMessageType> getAllMessageTypes() {
        List<EesMessageType> messageTypes = eesMessageTypeRepository.findAll();
        return messageTypes;
    }

    public ResponseEntity<Object> deleteMessageTypeByCode(String code) {
        EesMessageType messageType = eesMessageTypeRepository.findByCode(code);
        if (messageType == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_TYPE_NOT_FOUND + code), HttpStatus.NOT_FOUND);
        }
        eesMessageTypeRepository.delete(messageType);
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_TYPE_DELETED), HttpStatus.OK);
    }

    public HttpEntity<Object> updateMessageTypeByCode(String code, EesMessageTypeRequest request) {
        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String prefixMessageTypeCode = EesUserResponse.EES_MESSAGE_TYPE_PREFIX + code;
        EesMessageType messageType = eesMessageTypeRepository.findByCode(prefixMessageTypeCode);
        if (messageType == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_TYPE_NOT_FOUND + code), HttpStatus.NOT_FOUND);
        } else {
            if (request.getCode().length() != 20) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_TYPE_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                // Added an extra check before checking for the duplicate entity code.
                if (!request.getCode().equals(prefixMessageTypeCode)) {
                    EesMessageType existingMessageType = eesMessageTypeRepository.findByCode(request.getCode().toUpperCase(Locale.ROOT));
                    if (existingMessageType != null) {
                        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_MESSAGE_TYPE_ALREADY_EXISTS, request.getCode())), HttpStatus.BAD_REQUEST);
                    }
                }
                messageType.setCode(request.getCode());
                messageType.setTitle(request.getTitle());
                messageType.setDescription(request.getDescription());
                messageType.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                messageType.setCreatedBy(user.get());
                eesMessageTypeRepository.save(messageType);
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_MESSAGE_TYPE_UPDATED), HttpStatus.OK);
            }
        }
    }
}
