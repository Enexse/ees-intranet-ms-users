package com.enexse.intranet.ms.users.enums;

public enum EesStatus {

    ACTIVE("active"),
    DISABLED("disabled");

    private String status;

    EesStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
