package com.enexse.intranet.ms.users.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EesUpdateCollaboratorInfoRequest {

    private String entityCode;
    private String departmentCode;
    private String professionCode;
    private String matricule;
    private String userType;
    private int experience;
    private String roleCode;
    private boolean ticketRestaurant;
    private String typeTicketRestaurant;
}
