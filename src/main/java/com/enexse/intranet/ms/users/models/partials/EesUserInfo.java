package com.enexse.intranet.ms.users.models.partials;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-user-infos")
@Builder
public class EesUserInfo {

    @Id
    private String userId;

    private String[] defaultAvatar;
    private byte[] avatar;
    private String passwordChangedAt;
    private String collaboratorId;
    private String userType;

    @Builder.Default
    private String language = EesUserConstants.EES_DEFAULT_LANGUAGE;

    private String website;

    @Builder.Default
    private boolean auth2factory = false;

    @Builder.Default
    private boolean ticketRestaurant = false;

    private String typeTicketRestaurant;
    private String linkedIn;
    private String facebook;
    private String twitter;
    private String instagram;
}
