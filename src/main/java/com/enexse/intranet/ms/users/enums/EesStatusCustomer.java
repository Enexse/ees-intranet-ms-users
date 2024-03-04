package com.enexse.intranet.ms.users.enums;

public enum EesStatusCustomer {
    ACTIVE("active"),
    DISABLED("disabled");

    private String status;

    EesStatusCustomer(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
