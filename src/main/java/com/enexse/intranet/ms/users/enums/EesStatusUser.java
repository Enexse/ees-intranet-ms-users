package com.enexse.intranet.ms.users.enums;

public enum EesStatusUser {

    ACTIVE("active"),
    DISABLED("disabled"),
    PENDING("pending"),
    ARCHIVED ("archived") ,
    NEVER_CONNECTED("never_connected"),
    NOT_ASSIGNED("EES_NOT_ASSIGNED") ,

    CERTIFIED("EES_CERTFIED") ;


    private String status;

    EesStatusUser(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
