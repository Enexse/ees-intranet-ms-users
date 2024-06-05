package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserRequest;
import com.enexse.intranet.ms.users.models.EesUserSubRequest;
import com.enexse.intranet.ms.users.payload.request.EesSubRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesRequestRepository;
import com.enexse.intranet.ms.users.repositories.EesSubRequestRepository;
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
public class EesSubRequestService {

    private EesSubRequestRepository eesSubRequestRepository;
    private EesRequestRepository eesRequestRepository;
    private EesUserRepository eesUserRepository;

    public ResponseEntity<Object> insertSubRequest(EesSubRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!request.getRequestCode().startsWith(EesUserResponse.EES_REQUEST_PREFIX) || request.getRequestCode().length() != 11) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }
        EesUserRequest userRequest = eesRequestRepository.findByCode(request.getRequestCode().toUpperCase(Locale.ROOT));
        if (userRequest == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_NOT_FOUND, request.getRequestCode())), HttpStatus.BAD_REQUEST);
        }
        if (!request.getSubRequestCode().startsWith(EesUserResponse.EES_REQUEST_PREFIX) || request.getSubRequestCode().length() != 15) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_SUB_REQUEST_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }
        Optional<EesUserSubRequest> subRequest = eesSubRequestRepository.findByCode(request.getSubRequestCode().toUpperCase(Locale.ROOT));
        if (subRequest.isPresent()) {
            System.out.println(subRequest);
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_SUB_REQUEST_ALREADY_EXISTS, request.getSubRequestCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesUserSubRequest userSubRequest = new EesUserSubRequest()
                    .builder()
                    .subRequestCode(request.getSubRequestCode())
                    .description(request.getDescription())
                    .eesUserRequest(userRequest)
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesSubRequestRepository.save(userSubRequest);
            // Add the subRequest to the request
            userRequest.getSubRequests().add(userSubRequest);
            eesRequestRepository.save(userRequest);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_SUB_REQUEST_CREATED), HttpStatus.CREATED);
        }
    }

    public List<EesUserSubRequest> getAllSubRequests() {
        List<EesUserSubRequest> subRequests = eesSubRequestRepository.findAll()
                .stream().sorted(Comparator.comparing(EesUserSubRequest::getCreatedAt).reversed()).collect(Collectors.toList());
        return subRequests;
    }

    public ResponseEntity<Object> getAllSubRequestsByRequestCode(String requestCode) {
        if (requestCode.isEmpty()) {
            return new ResponseEntity<Object>(null, HttpStatus.OK);
        }
        EesUserRequest userRequest = eesRequestRepository.findByCode(requestCode);
        if (userRequest == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_NOT_FOUND, requestCode)), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<Object>(userRequest.getSubRequests(), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getAllSubRequestsByRequestId(String requestId) {
        if (requestId.isEmpty()) {
            return new ResponseEntity<Object>(null, HttpStatus.OK);
        }
        Optional<EesUserRequest> userRequest = eesRequestRepository.findById(requestId);
        if (userRequest.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_NOT_FOUND, requestId)), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<Object>(userRequest.get().getSubRequests(), HttpStatus.OK);
        }
    }

    public Optional<EesUserSubRequest> returnSubRequest(String subRequestId) {
        Optional<EesUserSubRequest> eesSubRequest = eesSubRequestRepository.findBySubRequestId(subRequestId);
        return eesSubRequest;
    }

    public ResponseEntity<Object> updateSubRequest(String subRequestCode, EesSubRequest updatedSubRequest) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(updatedSubRequest.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, updatedSubRequest.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String prefixRequestCode = EesUserResponse.EES_REQUEST_PREFIX + updatedSubRequest.getRequestCode();
        EesUserRequest userRequest = eesRequestRepository.findByCode(prefixRequestCode);
        if (userRequest == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_NOT_FOUND + prefixRequestCode), HttpStatus.NOT_FOUND);
        } else {
            if (prefixRequestCode.length() != 11) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                String prefixSubRequestCode = prefixRequestCode + "-" + subRequestCode;
                Optional<EesUserSubRequest> userSubRequest = eesSubRequestRepository.findByCode(prefixSubRequestCode);
                if (userSubRequest.isEmpty()) {
                    return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_SUB_REQUEST_NOT_FOUND + prefixSubRequestCode), HttpStatus.NOT_FOUND);
                } else {
                    // add extra check for the duplicate subRequest code
                    if (!updatedSubRequest.getSubRequestCode().equals(prefixSubRequestCode)) {
                        Optional<EesUserSubRequest> existingRequest = eesSubRequestRepository.findByCode(updatedSubRequest.getSubRequestCode().toUpperCase(Locale.ROOT));
                        if (existingRequest.isPresent()) {
                            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_SUB_REQUEST_ALREADY_EXISTS, updatedSubRequest.getSubRequestCode())), HttpStatus.BAD_REQUEST);
                        }
                    }
                    //update the subRequest
                    EesUserSubRequest oldSubRequest = userSubRequest.get();
                    oldSubRequest.setSubRequestCode(updatedSubRequest.getSubRequestCode());
                    oldSubRequest.setDescription(updatedSubRequest.getDescription());
                    oldSubRequest.setEesUserRequest(userRequest);
                    oldSubRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                    oldSubRequest.setCreatedBy(user.get());
                    eesSubRequestRepository.save(oldSubRequest);

                    //update the subRequests list in the EesUserRequest
                    List<EesUserSubRequest> subRequests = userRequest.getSubRequests();
                    int index = subRequests.indexOf(oldSubRequest);
                    if (index >= 0) {
                        subRequests.set(index, oldSubRequest);
                    }
                    userRequest.setSubRequests(subRequests);
                    userRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                    eesRequestRepository.save(userRequest);

                    return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_ID_SUB_REQUEST), HttpStatus.OK);
                }
            }
        }
    }


    public ResponseEntity<Object> deleteSubRequestByCode(String subRequestCode) {

        Optional<EesUserSubRequest> userSubRequest = eesSubRequestRepository.findByCode(subRequestCode);

        if (userSubRequest.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_SUB_REQUEST_NOT_FOUND + subRequestCode), HttpStatus.NOT_FOUND);
        } else {

            //delete the SubRequest in EesUserSubRequest
            eesSubRequestRepository.delete(userSubRequest.get());

            //delete the SubRequest in the EesUserRequest
            EesUserRequest userRequest = eesRequestRepository.findByCode(userSubRequest.get().getEesUserRequest().getRequestCode());
            List<EesUserSubRequest> subRequests = userRequest.getSubRequests();
            int index = subRequests.indexOf(userSubRequest.get());
            if (index >= 0) {
                subRequests.remove(index);
            }
            userRequest.setSubRequests(subRequests);
            userRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            eesRequestRepository.save(userRequest);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_SUB_REQUEST_DELETED), HttpStatus.OK);
        }
    }

    public EesUserSubRequest getSubRequestByCode(String requestCode) {
        return eesSubRequestRepository.findByCode(requestCode).get();
    }
}
