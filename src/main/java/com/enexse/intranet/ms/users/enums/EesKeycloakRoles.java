package com.enexse.intranet.ms.users.enums;

import lombok.Data;

public enum EesKeycloakRoles {

    ADMIN("EES-ADMINISTRATOR"),
    COLL("EES-COLLABORATOR"),
    RES("EES-RESPONSABLE");


    private String role;

    EesKeycloakRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
