package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserRequest;
import com.enexse.intranet.ms.users.models.EesUserSubRequest;
import com.enexse.intranet.ms.users.payload.request.EesRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesRequestRepository;
import com.enexse.intranet.ms.users.repositories.EesSubRequestRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EesRequestService {

    private EesRequestRepository eesRequestRepository;
    private EesSubRequestRepository eesSubRequestRepository;
    private EesUserRepository eesUserRepository;

    public ResponseEntity<Object> insertRequest(EesRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!request.getRequestCode().startsWith(EesUserResponse.EES_REQUEST_PREFIX) || request.getRequestCode().length() != 11) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
        }
        EesUserRequest existingRequest = eesRequestRepository.findByCode(request.getRequestCode().toUpperCase(Locale.ROOT));

        if (existingRequest != null) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_ALREADY_EXISTS, request.getRequestCode())), HttpStatus.BAD_REQUEST);
        } else {
            EesUserRequest userRequest = new EesUserRequest()
                    .builder()
                    .requestCode(request.getRequestCode().toUpperCase(Locale.ROOT))
                    .requestTitle(request.getRequestTitle())
                    .createdAt(EesCommonUtil.generateCurrentDateUtil())
                    .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                    .createdBy(user.get())
                    .build();
            eesRequestRepository.save(userRequest);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_CREATED), HttpStatus.CREATED);
        }
    }

    public Optional<EesUserRequest> returnRequest(String requestId) {
        Optional<EesUserRequest> eesSubRequest = eesRequestRepository.findById(requestId);
        return eesSubRequest;
    }

    public List<EesUserRequest> getAllRequests() {
        List<EesUserRequest> requests = eesRequestRepository.findAll();
        return requests;
    }

    public EesUserRequest getRequestByCode(String requestCode) {
        if (!requestCode.startsWith(EesUserResponse.EES_REQUEST_PREFIX)) {
            requestCode = EesUserResponse.EES_REQUEST_PREFIX + requestCode;
        }
        return eesRequestRepository.findByCode(requestCode);
    }

    public ResponseEntity<Object> updateRequestByCode(String requestCode, EesRequest request) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(request.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String prefixRequestCode = EesUserResponse.EES_REQUEST_PREFIX + requestCode;
        EesUserRequest userRequest = eesRequestRepository.findByCode(prefixRequestCode);
        if (userRequest == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_NOT_FOUND + requestCode), HttpStatus.NOT_FOUND);
        } else {
            if (request.getRequestCode().length() != 11) {
                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_INVALID_LENGTH), HttpStatus.BAD_REQUEST);
            } else {
                // add extra check for the duplicate request code
                if (!request.getRequestCode().equals(prefixRequestCode)) {
                    EesUserRequest existingRequest = eesRequestRepository.findByCode(request.getRequestCode().toUpperCase(Locale.ROOT));
                    if (existingRequest != null) {
                        return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_REQUEST_ALREADY_EXISTS, request.getRequestCode())), HttpStatus.BAD_REQUEST);
                    }
                }
                userRequest.setRequestCode(request.getRequestCode());
                userRequest.setRequestTitle(request.getRequestTitle());
                userRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                userRequest.setCreatedBy(user.get());
                eesRequestRepository.save(userRequest);

                //update subRequests in EesUserSubRequest model
                List<EesUserSubRequest> subRequests = userRequest.getSubRequests();
                for (EesUserSubRequest subRequest : subRequests) {

                    subRequest.setEesUserRequest(userRequest);
                }
                eesSubRequestRepository.saveAll(subRequests);

                return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_REQUEST), HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> deleteRequestByCode(String requestCode) {

        EesUserRequest request = eesRequestRepository.findByCode(EesUserResponse.EES_REQUEST_PREFIX + requestCode);
        if (request == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_NOT_FOUND + requestCode), HttpStatus.NOT_FOUND);
        } else {
            //delete the request in the EesUserRequest
            eesRequestRepository.delete(request);

            //delete the subRequests in the EesUserSubRequest
            List<EesUserSubRequest> subRequests = request.getSubRequests();
            eesSubRequestRepository.deleteAll(subRequests);

            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_REQUEST_DELETED), HttpStatus.OK);
        }
    }

    public Optional<EesUserRequest> getAllRequestsById(String requestId) {
        Optional<EesUserRequest> eesRequests = eesRequestRepository.findByRequestId(requestId);
        return eesRequests;
    }
}
