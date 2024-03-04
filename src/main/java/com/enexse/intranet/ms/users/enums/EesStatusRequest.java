package com.enexse.intranet.ms.users.enums;

public enum EesStatusRequest {

    PENDING("pending"),
    ACCEPTED("accepted"),
    BLOCKED("blocked"),
    REFUSED("refused"),
    SENT ("SENT") ;

    private String status;

    EesStatusRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
