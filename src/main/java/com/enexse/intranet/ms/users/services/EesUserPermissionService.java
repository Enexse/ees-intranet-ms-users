package com.enexse.intranet.ms.users.services;

import com.enexse.intranet.ms.users.constants.EesUserResponse;
import com.enexse.intranet.ms.users.models.EesUser;
import com.enexse.intranet.ms.users.models.EesUserPermission;
import com.enexse.intranet.ms.users.models.EesUserRole;
import com.enexse.intranet.ms.users.payload.request.EesPermissionAccessRequest;
import com.enexse.intranet.ms.users.payload.request.EesPermissionRequest;
import com.enexse.intranet.ms.users.payload.response.EesMessageResponse;
import com.enexse.intranet.ms.users.repositories.EesRoleRepository;
import com.enexse.intranet.ms.users.repositories.EesUserPermissionRepository;
import com.enexse.intranet.ms.users.repositories.EesUserRepository;
import com.enexse.intranet.ms.users.utils.EesCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
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
public class EesUserPermissionService {

    @Autowired
    private EesUserPermissionRepository permissionRepository;
    private EesRoleRepository roleRepository;
    private EesUserRepository eesUserRepository;

    private MongoOperations mongoOperations;

    public ResponseEntity<Object> insertPermission(EesPermissionRequest permissionRequest) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(permissionRequest.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, permissionRequest.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        String permissionCode = permissionRequest.getPermissionCode();

        // Check if permissionCode is null or empty
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PERMISSION_CODE_COULD_NOT_BE_NULL), HttpStatus.BAD_REQUEST);
        }

        // Check if permissionCode starts with "EES-PERMISSION-"
        if (!permissionCode.startsWith(EesUserResponse.EES_PERMISSION_PREFIX)) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PERMISSION_CODE_INVALID_FORMAT), HttpStatus.BAD_REQUEST);
        }

        // Check if permissionCode is already used
        if (permissionRepository.existsByPermissionCode(permissionCode)) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_PERMISSION_ALREADY_EXISTS, permissionRequest.getPermissionCode())), HttpStatus.BAD_REQUEST);
        }

        // Create a new permission entity
        EesUserPermission permission = EesUserPermission
                .builder()
                .permissionCode(permissionRequest.getPermissionCode().toUpperCase(Locale.ROOT))
                .permissionTitle(permissionRequest.getPermissionTitle())
                .permissionDescription(permissionRequest.getPermissionDescription())
                .createdAt(EesCommonUtil.generateCurrentDateUtil())
                .updatedAt(EesCommonUtil.generateCurrentDateUtil())
                .createdBy(user.get())
                .build();

        // Save the permission entity
        try {
            permissionRepository.save(permission);
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PERMISSION_CREATED), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new EesMessageResponse("an error ocured"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<EesUserPermission> getAllPermissions() {
        List<EesUserPermission> permissions = permissionRepository.findAll()
                .stream().sorted(Comparator.comparing(EesUserPermission::getCreatedAt).reversed()).collect(Collectors.toList());
        return permissions;
    }

    public EesUserPermission getPermissionsByCode(String permissionCode) {
        if (!permissionCode.startsWith(EesUserResponse.EES_PERMISSION_PREFIX)) {
            permissionCode = EesUserResponse.EES_PERMISSION_PREFIX + permissionCode;
        }

        if ((permissionRepository.findByPermissionCode(permissionCode)) == null) {
            return null;
        } else
            return permissionRepository.findByPermissionCode(permissionCode);
    }

    public ResponseEntity<Object> deletePermissionByCode(String permissionCode) {
        // Find the permission to delete
        EesUserPermission permission = permissionRepository.findByPermissionCode(EesUserResponse.EES_PERMISSION_PREFIX + permissionCode);

        if (permission == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PERMISSION_NOT_FOUND), HttpStatus.NOT_FOUND);
        } else {
            // Remove the permission from the associated Role entity
            EesUserRole role = roleRepository.findByPermissionsContains(permission);
            if (role != null) {
                //role.getPermissions().remove(permission);
                // Remove the permission from the permissions list and permissionAccessRequest list in the Role entity
                List<EesPermissionAccessRequest> accessRequests = role.getPermissionAccessRequest();
                List<EesUserPermission> rolePermissions = role.getPermissions();
                EesPermissionAccessRequest requestToDelete = null;

                // Find the request to delete from the accessRequests list
                for (EesPermissionAccessRequest req : accessRequests) {
                    if (req.getPermissionCode().equals(permission.getPermissionCode())) {
                        requestToDelete = req;
                        break;
                    }
                }

                // If the request to delete is found, remove it from both lists
                if (requestToDelete != null) {
                    accessRequests.remove(requestToDelete);
                    rolePermissions.removeIf(rolePermission -> rolePermission.getPermissionCode().equals(permission.getPermissionCode()));

                    // Update the lists in the Role entity
                    role.setPermissionAccessRequest(accessRequests);
                    role.setPermissions(rolePermissions);

                    // Save the updated Role entity
                    roleRepository.save(role);
                }
            }
            // Delete the permission
            permissionRepository.delete(permission);
        }
        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PERMISSION_DELETED), HttpStatus.OK);
    }


    public String getPermissionTitleByCode(String permissionCode) {
        EesUserPermission permission = permissionRepository.findByPermissionCode(permissionCode);
        if (permission != null) {
            return permission.getPermissionTitle();
        } else {
            return "Permission code not found";
        }
    }


    public ResponseEntity<Object> updatePermissionByCode(String permissionCode, EesPermissionRequest permissionRequest) {

        Optional<EesUser> user = null;
        user = eesUserRepository.findByEnexseEmail(permissionRequest.getCreatedBy());

        if (user.isEmpty()) {
            return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_USER_NOT_FOUND, permissionRequest.getCreatedBy())), HttpStatus.BAD_REQUEST);
        }

        if (!permissionCode.startsWith(EesUserResponse.EES_PERMISSION_PREFIX)) {
            permissionCode = EesUserResponse.EES_PERMISSION_PREFIX + permissionCode;
        }
        EesUserPermission permission = permissionRepository.findByPermissionCode(permissionCode);
        if (permission == null) {
            return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_PERMISSION_NOT_FOUND + "" + permissionCode), HttpStatus.NOT_FOUND);
        } else {
            if (!permissionRequest.getPermissionCode().equals(permissionCode)) {
                EesUserPermission existingPermission = permissionRepository.findByPermissionCode(permissionRequest.getPermissionCode().toUpperCase(Locale.ROOT));
                if (existingPermission != null) {
                    return new ResponseEntity<Object>(new EesMessageResponse(String.format(EesUserResponse.EES_PERMISSION_ALREADY_EXISTS, permissionRequest.getPermissionCode())), HttpStatus.BAD_REQUEST);
                }
            }
        }

        permission.setPermissionCode(permissionRequest.getPermissionCode());
        permission.setPermissionTitle(permissionRequest.getPermissionTitle());
        permission.setPermissionDescription(permissionRequest.getPermissionDescription());
        permission.setUpdatedAt(EesCommonUtil.generateCurrentDateUtil());
        permission.setCreatedBy(user.get());
        permissionRepository.save(permission);

        return new ResponseEntity<Object>(new EesMessageResponse(EesUserResponse.EES_UPDATE_BY_CODE_PERMISSION), HttpStatus.OK);
    }
}



