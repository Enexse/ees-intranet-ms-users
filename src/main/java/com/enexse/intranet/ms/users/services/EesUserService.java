package com.enexse.intranet.ms.users.services;


import com.enexse.intranet.ms.users.configurations.keycloakProvider;
import com.enexse.intranet.ms.users.constants.EesFrontEndLink;
import com.enexse.intranet.ms.users.constants.EesUserConstants;
import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.enums.EesStatusRequest;
import com.enexse.intranet.ms.users.enums.EesStatusUser;
import com.enexse.intranet.ms.users.exceptions.ObjectFoundException;
import com.enexse.intranet.ms.users.models.*;
import com.enexse.intranet.ms.users.models.partials.EesUserInfo;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetActivity;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetContractHours;
import com.enexse.intranet.ms.users.models.timesheet.EesTimeSheetWorkplace;
import com.enexse.intranet.ms.users.openfeign.EesWorkplaceService;
import com.enexse.intranet.ms.users.payload.request.*;
import com.enexse.intranet.ms.users.payload.request.timesheet.EesUserTimesheetRequest;
import com.enexse.intranet.ms.users.payload.response.EesGenericResponse;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.*;
import com.enexse.intranet.ms.users.repositories.partials.EesUserInfoRepository;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetActivityRepository;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetContractHoursRepository;
import com.enexse.intranet.ms.users.repositories.timesheet.EesTimeSheetWorkPlaceRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import com.enexse.intranet.ms.users.utils.ImageUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EesUserService {

    @Value("${keycloak.realm}")
    public String realm;

    private EesUserRepository eesUserRepository;
    @Autowired
    private EesUserAddressRepository eesUserAddressRepository;
    @Autowired
    private EesUserContactRepository eesUserContactRepository;
    @Autowired
    private EesUserDepartmentRepository eesUserDepartmentRepository;
    @Autowired
    private EesUserEntityRepository eesUserEntityRepository;
    @Autowired
    private EesRoleRepository eesRoleRepository;
    @Autowired
    private EesMailService eesMailService;
    @Autowired
    private EesVerifyIdentityRepository eesVerifyIdentityRepository;
    @Autowired
    private EesCreateRequestRepository eesCreateRequestRepository;
    @Autowired
    private EesRequestRepository eesRequestRepository;
    @Autowired
    private EesSubRequestRepository eesSubRequestRepository;
    @Autowired
    private EesCustomerRepository eesCustomerRepository;
    @Autowired
    private EesTimeSheetActivityRepository eesTimeSheetActivityRepository;
    @Autowired
    private EesTimeSheetWorkPlaceRepository eesTimeSheetWorkPlaceRepository;
    @Autowired
    private EesTimeSheetContractHoursRepository eesTimeSheetContractHoursRepository;
    @Autowired
    private EesUserInfoRepository eesUserInfoRepository;
    @Autowired
    private EesUserProfessionRepository eesUserProfessionRepository;

    @Autowired
    private EesMissionRepository eesMissionRepository;
    @Autowired
    private keycloakProvider kcProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EesFormationRepository eesFormationRepository;

    @Autowired
    private EesWorkplaceService eesWorkplaceService;

    public EesUserService(EesUserRepository userRepository) {
        this.eesUserRepository = userRepository;
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public static boolean datePassedLimit(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime inputDate = LocalDateTime.parse(date, formatter);

        LocalDateTime currentDate = LocalDateTime.now();

        long days = java.time.Duration.between(inputDate, currentDate).toDays();

        return days > 90;
    }

    public static int getMonthNumberFromString(String dateString) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, dtf);
        int monthNumber = dateTime.getMonthValue();
        return monthNumber;
    }

    public boolean verifyPersonalEmail(String email) {
        Optional<EesUser> eesUser = eesUserRepository.findByPersonalEmail(email);
        return eesUser.isEmpty();
    }

    public Optional<EesUser> returnUser(String email) {
        Optional<EesUser> eesUser = eesUserRepository.findByPersonalEmail(email);
        return eesUser;
    }

    public Optional<EesUser> getUserByEnexseEmail(String email) {
        Optional<EesUser> eesUser = eesUserRepository.findByEnexseEmail(email);
        return eesUser;
    }

    public ResponseEntity<Object> disableUser(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            user.get().setStatus(EesStatusUser.DISABLED);

            user.get().setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            eesUserRepository.save(user.get());
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public Optional<EesUser> getUserById(String id) {
        Optional<EesUser> eesUser = eesUserRepository.findById(id);
        return eesUser;
    }

    public ResponseEntity<Object> updateLastLogin(String id, String date) {
        Optional<EesUser> eesUser = eesUserRepository.findById(id);
        if (eesUser.isPresent()) {
            eesUser.get().setLastLogin(date);
//            if(eesUser.get().getStatus().compareTo(EesStatusUser.ACTIVE) == 0){
//                eesUser.get().setStatus(EesStatusUser.PENDING);
//            }
            eesUserRepository.save(eesUser.get());
            return ResponseEntity.ok(eesUser.get());
        } else {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        }
    }

    public ResponseEntity<Object> updatePassword(String userId, String newPassword, String idlink) {
        EesUser user;
        EesUser updatedUser;
        if (getUserById(userId).isEmpty()) {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else {
            //update password in database
            user = getUserById(userId).get();
            user.setPassword(passwordEncoder.encode(newPassword));
            updatedUser = eesUserRepository.save(user);
            eesVerifyIdentityRepository.deleteById(idlink);
        }
        return ResponseEntity.ok(updatedUser);

    }

    public Page<EesUser> getLastCollaboratorsPaginated(int page, int pageSize) {

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return eesUserRepository.findAll(pageRequest);
    }

    public Page<EesUser> paginateCollaboratorsFiltred(List<String> ids, int page, int pageSize) {
        int startIdx = page * pageSize;
        int endIdx = Math.min(startIdx + pageSize, ids.size());
        List<EesUser> collaborators = new ArrayList<>();

        for (String id : ids) {
            Optional<EesUser> user = eesUserRepository.findByUserId(id);
            user.ifPresent(collaborators::add);
        }

        collaborators.sort(Comparator.comparing(EesUser::getCreatedAt).reversed());

        List<EesUser> pagedCollaborators = collaborators.subList(startIdx, endIdx);

        return new PageImpl<>(pagedCollaborators, PageRequest.of(page, pageSize), collaborators.size());
    }

    public ResponseEntity<Object> getAllCollaborators() {
        List<EesUser> list = null;
        list = eesUserRepository.findAll();
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<Object> updateAvatar(String userId, MultipartFile avatar) {
        try {
            Optional<EesUser> optionalUser = eesUserRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            EesUser user = optionalUser.get();

            if (avatar.isEmpty()) {
                return new ResponseEntity<>("No avatar uploaded", HttpStatus.BAD_REQUEST);
            }

            // Check file size
            if (avatar.getSize() > EesUserConstants.EES_AVATAR_MAX_SIZE) {
                return new ResponseEntity<>("your Avatar size (" + avatar.getSize() / (1024 * 1024) + " MB) exceeds the limit of 3MB", HttpStatus.BAD_REQUEST);
            }

            // Check file format
            String originalFilename = avatar.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            List<String> allowedExtensions = EesUserConstants.EES_ALLOWED_EXTENTIONS;
            if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                StringBuilder extentions = new StringBuilder();
                for (String ex : allowedExtensions) {
                    extentions.append(ex).append(" ");
                }
                return new ResponseEntity<>("Invalid avatar format. Allowed formats: " + extentions, HttpStatus.BAD_REQUEST);
            }

            EesUserInfo userInfo = user.getEesUserInfo();
            if (userInfo.getAvatar() == null) {
                userInfo.setAvatar(ImageUtil.compressAvatar(avatar.getBytes()));
            } else {
                userInfo.setAvatar(ImageUtil.compressAvatar(avatar.getBytes()));
            }

            eesUserInfoRepository.save(userInfo);

            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_AVATAR_UPLOADED_SUCCESSFULLY), HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload avatar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteAvatar(String userId) {
        EesUser user;
        if (getUserById(userId).isEmpty()) {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else {
            user = getUserById(userId).get();
            EesUserInfo userInfo = user.getEesUserInfo();
            userInfo.setAvatar(null);
            eesUserInfoRepository.save(userInfo);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_AVATAR_DELETED_SUCCESSFULLY), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> changePassword(String userId, String currentPassword, EesChangePasswordRequest request) throws Exception {
        EesUser user;
        EesUser updatedUser;
        if (getUserById(userId).isEmpty()) {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else {
            user = getUserById(userId).get();
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new Exception("Current password is incorrect");
            }
            // Update password in keycloak
            UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
            usersResource.get(user.getKeycloakId()).resetPassword(createPasswordCredentials(request.getNewPassword()));

            // Update password in the database
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            if (user.getStatus().compareTo(EesStatusUser.DISABLED) == 0) {
                user.setStatus(EesStatusUser.ACTIVE);
            }
            updatedUser = eesUserRepository.save(user);

            // Update field passwordChangedAt in EesUserInfo
            EesUserInfo userInfo = user.getEesUserInfo();
            userInfo.setPasswordChangedAt(EesCommonUtil.generateCurrentDateUtil());
            userInfo.setCollaboratorId(user.getUserId());
            eesUserInfoRepository.save(userInfo);
        }
        return ResponseEntity.ok(updatedUser);
    }

    public ResponseEntity<Object> changePersonalEmail(String userId, EesUserEmailRequest request) throws Exception {
        EesUser user;
        List<String> emails = getPersonalEmails();
        if (getUserById(userId).isEmpty()) {
            throw new Exception(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else if (emails.contains(request.getEmail())) {
            throw new Exception("Personal email already exists in database");
        } else {
            user = getUserById(userId).get();
            user.setPersonalEmail(request.getEmail());
            eesUserRepository.save(user);
        }
        return ResponseEntity.ok(user);
    }

    public List<String> getPersonalEmails() {
        List<String> emails = new ArrayList<>();
        List<EesUser> collabs = null;
        collabs = eesUserRepository.findAll();
        for (EesUser collab : collabs) {
            emails.add(collab.getPersonalEmail());
        }
        return emails;
    }

    public ResponseEntity<Object> insertUser(com.enexse.intranet.ms.users.payload.request.EesUserRequest request) {
        EesUserAddress userAddress = new EesUserAddress();
        Optional<EesUser> optionalEesUser = eesUserRepository.findByPersonalEmail(request.getPersonalEmail());

        userAddress.setCountry(request.getUserAddress().getCountry());
        userAddress.setState(request.getUserAddress().getState());
        userAddress.setAddressLine(request.getUserAddress().getAddressLine());
        userAddress.setZipCode(request.getUserAddress().getZipCode());

        EesUserContact userContact = new EesUserContact();
        userContact.setPrefixPhone(request.getUserContact().getPrefixPhone());
        userContact.setPhoneNumber(request.getUserContact().getPhoneNumber());

        EesUserInfo userInfo = new EesUserInfo()
                .builder()
                .defaultAvatar(EesCommonUtil.generateDefaultAvatar())
                .build();

        EesUserEntity entity = eesUserEntityRepository.findByEntityCode(request.getEntityCode());
        EesUserDepartment department = eesUserDepartmentRepository.findByCompanyDepartmentCode(request.getDepartmentCode());
        int monthNumber = getMonthNumberFromString(EesCommonUtil.generateCurrentDateUtil());
        if (department.getMap().containsKey(monthNumber)) {
            int value = department.getMap().get(monthNumber);
            department.getMap().put(monthNumber, value + 1);
        } else {
            department.getMap().put(monthNumber, 1);
        }
        EesUserProfession profession = eesUserProfessionRepository.findByProfessionCode(request.getProfessionCode());

        EesUser user = new EesUser()
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .pseudo(request.getLastName().split(" ")[0].toLowerCase(Locale.ROOT) + "." + request.getFirstName().split(" ")[0].toLowerCase(Locale.ROOT))
                .personalEmail(request.getPersonalEmail())
                .status(EesStatusUser.NEVER_CONNECTED)
                .gender(request.getGender())
                .notes(request.getNotes())
                .lastLogin(request.getLastLogin())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .deletedAt(request.getDeletedAt())
                .userAddress(userAddress)
                .userContact(userContact)
                .eesEntity(entity)
                .entityCode(request.getEntityCode())
                .eesDepartment(department)
                .departmentCode(request.getDepartmentCode())
                .eesProfession(profession)
                .professionCode(request.getProfessionCode())
                .eesUserInfo(userInfo)
                .experience(0)
                .build();

        com.enexse.intranet.ms.users.models.EesUserRequest userRequest = new com.enexse.intranet.ms.users.models.EesUserRequest();
        userRequest.setRequestCode("EES-DMD-INV");
        userRequest.setRequestTitle("Activation of the ENEXSE email address");
        userRequest.setCreatedAt(EesCommonUtil.generateCurrentDateUtil());
        userRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
        eesRequestRepository.save(userRequest);

        EesUserSubRequest userSubRequest = new EesUserSubRequest();
        userSubRequest.setSubRequestCode("EES-DMD-INV-COL");
        userSubRequest.setDescription("Demande de création de l'email " + user.getPersonalEmail() + " au format enexse");
        userSubRequest.setEesUserRequest(userRequest);
        userSubRequest.setCreatedAt(EesCommonUtil.generateCurrentDateUtil());
        userSubRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
        eesSubRequestRepository.save(userSubRequest);

        EesUserCreateRequest createRequest = new EesUserCreateRequest();

        createRequest.setRequest(userRequest);
        createRequest.setSubRequest(userSubRequest);
        createRequest.setStatus(EesStatusRequest.PENDING);
        createRequest.setPersonalEmail(user.getPersonalEmail());
        createRequest.setEnexseEmail(user.getEnexseEmail());
        createRequest.setPassword(user.getPassword());
        createRequest.setUser(user);
        createRequest.setType("Automatic");
        createRequest.setCreatedAt(EesCommonUtil.generateCurrentDateUtil());
        createRequest.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());

        if (verifyPersonalEmail(user.getPersonalEmail())) {
            eesUserAddressRepository.save(user.getUserAddress());
            eesUserContactRepository.save(user.getUserContact());
            eesUserInfoRepository.save(userInfo);
            eesUserRepository.save(user);
            eesUserDepartmentRepository.save(department);
            eesCreateRequestRepository.save(createRequest);
            eesRequestRepository.delete(userRequest);
            eesSubRequestRepository.delete(userSubRequest);
        } else {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_ALREADY_EXISTS, request.getPersonalEmail()));
        }

        // Send invitation email to user
        /*String tmpPassword = EesCommonUtil.generateFirstPassword(request.getLastName());*/

        System.out.println(user);
        //return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_CREATED), HttpStatus.CREATED);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> certificateEmailUser(EesUserEmailRequest request) {
        Optional<EesUser> user = eesUserRepository.findByEnexseEmail(request.getEmail());
        if (user.isPresent()) {
            EesVerifyIdentity verifyIdentity = new EesVerifyIdentity()
                    .builder()
                    .expiry_date(EesCommonUtil.generateExpirationLink())
                    .userEmail(request.getEmail())
                    .verifyType(EesUserConstants.EES_VERIFY_TYPE_EMAIL_VERIFICATION)
                    .userId(user.get().getUserId())
                    .link(UUID.randomUUID().toString())
                    .build();
            user.get().setStatus(EesStatusUser.CERTIFIED);
            ;
            eesVerifyIdentityRepository.save(verifyIdentity);
            String link = EesFrontEndLink.EES_LINK_CERTIFIED_EMAIL + verifyIdentity.getLink() + "&expirationDate=" + verifyIdentity.getExpiry_date() + "&verifyType="
                    + verifyIdentity.getVerifyType();
            ResponseEntity<Object> response = eesMailService.certificationUserEmail(request.getEmail(), link, EesUserConstants.EES_VERIFY_TYPE_EMAIL_VERIFICATION);
            if (response.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) {
                return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_CERTIFICATION_LINK, request.getEmail())), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_ERROR_SEND_EMAIL + request.getEmail()), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, request.getEmail())), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateUserProfil(String userId, EesUpdateUserProfile request) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        EesUserAddress address = user.get().getUserAddress();
        EesUserContact contact = user.get().getUserContact();
        if (user.isPresent()) {
            user.get().setFirstName(request.getFirstName());
            user.get().setLastName(request.getLastName());
            user.get().setPseudo(request.getLastName().split(" ")[0].toLowerCase(Locale.ROOT) + "." + request.getFirstName().split(" ")[0].toLowerCase(Locale.ROOT));
            address.setCountry(request.getCountry());
            address.setState(request.getState());
            address.setAddressLine(request.getAddressLine());
            address.setZipCode(request.getZipCode());
            eesUserAddressRepository.save(address);
            contact.setPhoneNumber(request.getPhoneNumber());
            contact.setPrefixPhone(request.getPrefixPhone());
            eesUserContactRepository.save(contact);
            eesUserRepository.save(user.get());
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> updateUser(String userId, EesUserTimesheetRequest request) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            EesCustomer customer = eesCustomerRepository.findByCustomerCode(request.getCustomerCode());
            EesTimeSheetActivity activity = eesTimeSheetActivityRepository.findByActivityCode(request.getActivityCode());
            EesTimeSheetWorkplace workplace = eesTimeSheetWorkPlaceRepository.findByWorkPlaceCode(request.getWorkplaceCode());
            EesTimeSheetContractHours contractHours = eesTimeSheetContractHoursRepository.findByContractHoursCode(request.getContractHoursCode());

            user.get().getEesUserTimesheet().setRtt(request.getRtt());
            user.get().getEesUserTimesheet().setBusinessManagerType(request.getBusinessManagerType());
            user.get().getEesUserTimesheet().setBusinessManagerEnexse(request.getBusinessManagerEnexse());
            user.get().getEesUserTimesheet().setBusinessManagerClient(request.getBusinessManagerClient());
            user.get().getEesUserTimesheet().setNumAffair(request.getNumAffair());
            user.get().getEesUserTimesheet().setEesCustomer(customer);
            user.get().getEesUserTimesheet().setTimeSheetActivity(activity);
            user.get().getEesUserTimesheet().setWorkplace(workplace);
            user.get().getEesUserTimesheet().setContractHours(contractHours);
            user.get().getEesUserTimesheet().setProjectName(request.getProjectName());

            eesUserRepository.save(user.get());

        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Object> activateOrDisabledUser(String userId, String link) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            Optional<EesVerifyIdentity> identity = eesVerifyIdentityRepository.findByLink(link);
            //user.get().setStatus(user.get().getStatus().compareTo(EesStatusUser.ACTIVE) == 0 ? EesStatusUser.DISABLED : EesStatusUser.ACTIVE);
            user.get().setStatus(EesStatusUser.CERTIFIED);
            user.get().setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
            eesUserRepository.save(user.get());
            eesVerifyIdentityRepository.delete(identity.get());
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> activateUser(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            EesStatusUser currentStatus = user.get().getStatus();
            if (currentStatus == EesStatusUser.CERTIFIED || currentStatus == EesStatusUser.DISABLED) {
                user.get().setStatus(EesStatusUser.ACTIVE);
                user.get().setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
                eesUserRepository.save(user.get());
                return ResponseEntity.ok(user);
            } else {
                return new ResponseEntity<Object>(new EesMessageResponse("User status cannot be changed to active."), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> archiveUser(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            user.get().setStatus(EesStatusUser.ARCHIVED);
            user.get().setDeletedAt(EesCommonUtil.generateCurrentDateUtil());
            eesUserRepository.save(user.get());
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_ARCHIVED)), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public boolean checkTemporaryPassword(String userId, String password) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            String temporaryPassword = user.get().getFirstName() + "@1234";
            if (password.equals(temporaryPassword)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean checkPasswordExpired(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            String date = user.get().getEesUserInfo().getPasswordChangedAt();
            if (datePassedLimit(date)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public ResponseEntity<Object> updateUserInfoLanguage(String userId, String language) {
        EesUserInfo userInfo = eesUserInfoRepository.findByUserId(userId);
        if (userInfo != null) {
            userInfo.setLanguage(language);
            eesUserInfoRepository.save(userInfo);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_INFO_LANGUAGE), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, userId)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateUserInfoAuth2Factory(String userId) {
        EesUserInfo userInfo = eesUserInfoRepository.findByUserId(userId);
        if (userInfo != null) {
            userInfo.setAuth2factory(userInfo.isAuth2factory() ? Boolean.FALSE : Boolean.TRUE);
            eesUserInfoRepository.save(userInfo);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_INFO_AUTH_2_FACTORY), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, userId)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> sendCode2FactoryUser(String enexseEmail) {
        Optional<EesUser> user = eesUserRepository.findByEnexseEmail(enexseEmail);
        if (user.isPresent()) {
            String code = EesCommonUtil.generateCodeTwoFactoryAuth();
            EesVerifyIdentity verifyIdentity = new EesVerifyIdentity()
                    .builder()
                    .expiry_date(EesCommonUtil.generateExpirationLink())
                    .userEmail(enexseEmail)
                    .verifyType(EesUserConstants.EES_VERIFY_TYPE_2FACTORY_AUTH_CODE)
                    .userId(user.get().getUserId())
                    .code(code)
                    .build();

            // First find code with the same verifyType
            Optional<EesVerifyIdentity> codeVerifyIdentity = eesVerifyIdentityRepository.findByUserEmailAndVerifyType(enexseEmail, EesUserConstants.EES_VERIFY_TYPE_2FACTORY_AUTH_CODE);
            if (codeVerifyIdentity.isPresent()) {
                eesVerifyIdentityRepository.delete(codeVerifyIdentity.get());
            }
            eesVerifyIdentityRepository.save(verifyIdentity);
            ResponseEntity<Object> response = eesMailService.sendCodeTwoFactoryAuthentification(enexseEmail, code, EesUserConstants.EES_VERIFY_TYPE_2FACTORY_AUTH_CODE);
            if (response.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) {
                return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_TWO_FACTORY_CODE, enexseEmail)), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_USER_ERROR_SEND_EMAIL + enexseEmail), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, enexseEmail)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> verificationCodeAuth(String enexseEmail, String code) {
        Optional<EesVerifyIdentity> userVerification = eesVerifyIdentityRepository.findByUserEmailAndCode(enexseEmail, code);
        if (userVerification.isPresent()) {
            if (new Date(userVerification.get().getExpiry_date()).after(new Date())) {
                return new ResponseEntity<Object>(new EesGenericResponse(Boolean.TRUE, EesUserResponse.EES_USER_CODE_VERIFICATION_OK), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new EesGenericResponse(Boolean.FALSE, EesUserResponse.EES_USER_CODE_VERIFICATION_KO), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_VERIFICATION_NOT_FOUND, enexseEmail, code)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateCollaboratorInfo(String userId, EesUpdateCollaboratorInfoRequest request) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            if (request.getEntityCode() != null) {
                EesUserEntity entity = eesUserEntityRepository.findByEntityCode(request.getEntityCode());
                user.get().setEesEntity(entity);
                user.get().setEntityCode(request.getEntityCode());
            }
            if (request.getDepartmentCode() != null) {
                EesUserDepartment department = eesUserDepartmentRepository.findByCompanyDepartmentCode(request.getDepartmentCode());

                //add a check whether user have already a department or no, if yes the number of users assigned to this department should decrease by one.
//                EesUserDepartment dep = user.get().getEesDepartment();
//                if (dep != null) {
//                    int i = 1;
//                    while (i < 13) {
//                        if (dep.getMap().containsKey(i)) {
//                            int value = department.getMap().get(i);
//                            department.getMap().put(i, value - 1);
//                            i += 12;
//                        }
//                        i++;
//                    }
//                }

                user.get().setEesDepartment(department);
                user.get().setDepartmentCode(request.getDepartmentCode());
//                int monthNumber = getMonthNumberFromString(EesCommonUtil.generateCurrentDateUtil());
//                if (department.getMap().containsKey(monthNumber)) {
//                    int value = department.getMap().get(monthNumber);
//                    department.getMap().put(monthNumber, value + 1);
//                } else {
//                    department.getMap().put(monthNumber, 1);
//                }
                eesUserDepartmentRepository.save(department);
            }
            if (request.getProfessionCode() != null) {
                EesUserProfession profession = eesUserProfessionRepository.findByProfessionCode(request.getProfessionCode());
                user.get().setEesProfession(profession);
                user.get().setProfessionCode(request.getProfessionCode());
            }
            if (request.getMatricule() != null) {
                Optional<EesUser> collaborator = null;
                collaborator = eesUserRepository.findByMatricule(request.getMatricule());
                if (collaborator.isEmpty()) {
                    user.get().setMatricule(request.getMatricule());
                } else if (Objects.equals(collaborator.get().getUserId(), userId)) {
                    //do nothing
                } else {
                    throw new ObjectFoundException(String.format(EesUserResponse.EES_MATRICULE_ALREADY_EXISTS, request.getMatricule(), HttpStatus.BAD_REQUEST));
                }
            }
            if (request.getExperience() != 0) {
                user.get().setExperience(request.getExperience());
            }
            if (request.getRoleCode() != null) {

                //get the previous role if it exists
                EesUserRole previousRole = user.get().getRole();
                if (previousRole != null) {

                    //retrieve userId from previous role
                    int index = previousRole.getUsers().indexOf(user.get().getUserId());
                    if (index >= 0) {
                        previousRole.getUsers().remove(index);
                    }
                    eesRoleRepository.save(previousRole);

                    //retrieve the previous role from user in keycloak
                    String previousKeycloakRole = user.get().getRole().getKeycloakRole();
                    retrieveUserRole(user.get().getUserId(), previousKeycloakRole);
                }

                //add userId to new role (role from request)
                EesUserRole role = eesRoleRepository.findByCode(request.getRoleCode());
                role.getUsers().add(user.get().getUserId());
                eesRoleRepository.save(role);

                //assign new role to user in database and keycloak
                user.get().setRole(role);
                assignUserRole(user.get().getUserId(), role.getKeycloakRole());
            }

            // Update user infos
            EesUserInfo userInfo = eesUserInfoRepository.findByCollaboratorId(userId);
            if (userInfo != null) {
                userInfo.setUserType(request.getUserType());
                userInfo.setTicketRestaurant(request.isTicketRestaurant());
                userInfo.setTypeTicketRestaurant(request.getTypeTicketRestaurant());
                eesUserInfoRepository.save(userInfo);
            }

            eesUserRepository.save(user.get());
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user.get());
    }

    public boolean assignUserRole(String userId, String roleName) {
        EesUser user;
        if (getUserById(userId).isEmpty()) {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else {
            user = getUserById(userId).get();
            try {
                RoleRepresentation roleRepresentation = kcProvider.getInstance().realm(realm).roles().get(roleName).toRepresentation();
                kcProvider.getInstance().realm(realm).users().get(user.getKeycloakId()).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    public boolean retrieveUserRole(String userId, String roleName) {
        EesUser user;
        if (getUserById(userId).isEmpty()) {
            throw new ObjectFoundException(String.format(EesUserResponse.EES_USER_DOES_NOT_EXISTS));
        } else {
            user = getUserById(userId).get();
            try {
                RoleRepresentation roleRepresentation = kcProvider.getInstance().realm(realm).roles().get(roleName).toRepresentation();
                kcProvider.getInstance().realm(realm).users().get(user.getKeycloakId()).roles().realmLevel().remove(Collections.singletonList(roleRepresentation));
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    public ResponseEntity<Object> getEntity(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            EesUserEntity entity = user.get().getEesEntity();
            return ResponseEntity.ok(entity);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<Object> getDepartment(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            EesUserDepartment department = user.get().getEesDepartment();
            return ResponseEntity.ok(department);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<Object> getRole(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            EesUserRole role = user.get().getRole();
            return ResponseEntity.ok(role);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<Object> getProfession(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            EesUserProfession profession = user.get().getEesProfession();
            return ResponseEntity.ok(profession);
        } else {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }

    }

    public List<EesUser> getLastUsers() {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 3, sort);

        return eesUserRepository.findLastThreeUsersAfterDate(EesCommonUtil.generateCurrentDateUtil(), pageable);

    }

    public ResponseEntity<Object> addMissionToCollaborator(String userId, EesMissionRequest request) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);

        if (user.isPresent()) {
            // Create a new mission object
            EesMission newMission = new EesMission();
            newMission.setProject(request.getProject());
            //EesTimeSheetWorkplace workplace = eesWorkplaceService.findByCode(request.getWorkplaceCode());
            newMission.setPlace(request.getWorkplaceCode());
            EesCustomer client = eesCustomerRepository.findByCustomerId(request.getCustomerId());

            newMission.setCustomer(client);
            newMission.setStartDate(request.getStartDate());
            newMission.setEndDate(request.getEndDate());
            eesMissionRepository.save(newMission);

            if (user.get().getMissions() == null) {
                user.get().setMissions(new ArrayList<>());
            }

            // Add the mission to the user's mission list
            user.get().getMissions().add(newMission);

            // Save the updated user
            eesUserRepository.save(user.get());

            return ResponseEntity.ok(user.get());
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Object> preventMissionFromCollaborator(String userId, String missionId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            List<EesMission> missions = user.get().getMissions();

            // Find the mission with the missionId
            Optional<EesMission> mission = missions.stream()
                    .filter(m -> m.getMissionId().equals(missionId))
                    .findFirst();

            if (mission.isPresent()) {
                // Remove the mission from the list
                missions.remove(mission.get());

                // Save the updated user
                eesUserRepository.save(user.get());

                return ResponseEntity.ok("Mission removed from the collaborator.");
            } else {
                return new ResponseEntity<>("Mission not found for the collaborator.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Object> retrieveMissionsFromCollaborator(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            List<EesMission> missions = user.get().getMissions();
            return ResponseEntity.ok(missions);
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateMission(String userId, String missionId, EesMissionRequest request) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            List<EesMission> missions = user.get().getMissions();


            Optional<EesMission> mission = missions.stream()
                    .filter(m -> m.getMissionId().equals(missionId))
                    .findFirst();
            EesCustomer client = eesCustomerRepository.findByCustomerId(request.getCustomerId());
            //EesTimeSheetWorkplace workplace = eesTimeSheetWorkPlaceRepository.findByWorkPlaceCode(request.getWorkplaceCode());
            if (mission.isPresent()) {

                EesMission missionToUpdate = mission.get();
                missionToUpdate.setProject(request.getProject());
                missionToUpdate.setPlace(request.getWorkplaceCode());
                missionToUpdate.setStartDate(request.getStartDate());
                missionToUpdate.setEndDate(request.getEndDate());
                missionToUpdate.setCustomer(client);


                EesMission updatedMission = eesMissionRepository.save(missionToUpdate);

                return ResponseEntity.ok(updatedMission);
            } else {
                return new ResponseEntity<>("Mission not found for the collaborator.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateFormation(String userId, String formationId, EesFormationRequest request) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            List<EesFormation> formations = user.get().getFormations();

            Optional<EesFormation> formation = formations.stream()
                    .filter(f -> f.getFormationId().equals(formationId))
                    .findFirst();

            if (formation.isPresent()) {
                EesFormation formationToUpdate = formation.get();
                formationToUpdate.setFormationName(request.getFormationName());
                formationToUpdate.setOrganisme(request.getOrganisme());
                formationToUpdate.setCertification(request.getCertification());
                formationToUpdate.setStartDate(request.getStartDate());
                formationToUpdate.setEndDate(request.getEndDate());

                EesFormation updatedFormation = eesFormationRepository.save(formationToUpdate);

                return ResponseEntity.ok(updatedFormation);
            } else {
                return new ResponseEntity<>("Formation not found for the collaborator.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Object> addFormationToCollaborator(String userId, EesFormationRequest request) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            // Create a new formation object
            EesFormation newFormation = new EesFormation();
            newFormation.setFormationName(request.getFormationName());
            newFormation.setOrganisme(request.getOrganisme());
            newFormation.setCertification(request.getCertification());
            newFormation.setStartDate(request.getStartDate());
            newFormation.setEndDate(request.getEndDate());
            eesFormationRepository.save(newFormation);

            if (user.get().getFormations() == null) {
                user.get().setFormations(new ArrayList<>());
            }

            // Add the formation to the user's formation list
            user.get().getFormations().add(newFormation);

            // Save the updated user
            eesUserRepository.save(user.get());

            return ResponseEntity.ok(user.get());
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> preventFormationFromCollaborator(String userId, String formationId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            List<EesFormation> formations = user.get().getFormations();

            // Find the formation with the formationId
            Optional<EesFormation> formation = formations.stream()
                    .filter(f -> f.getFormationId().equals(formationId))
                    .findFirst();

            if (formation.isPresent()) {
                // Remove the formation from the list
                formations.remove(formation.get());

                // Save the updated user
                eesUserRepository.save(user.get());

                return ResponseEntity.ok("Formation removed from the collaborator.");
            } else {
                return new ResponseEntity<>("Formation not found for the collaborator.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> retrieveFormationsFromCollaborator(String userId) {
        Optional<EesUser> user = eesUserRepository.findByUserId(userId);
        if (user.isPresent()) {
            List<EesFormation> formations = user.get().getFormations();
            return ResponseEntity.ok(formations);
        } else {
            return new ResponseEntity<>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
    }


}
