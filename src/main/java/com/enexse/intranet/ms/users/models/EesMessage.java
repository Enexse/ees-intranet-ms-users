package com.enexse.intranet.ms.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-messages")
@Builder
public class EesMessage {

    @Id
    private String messageId;

    private String firstName;
    private String lastName;
    private String email;
    private String[] defaultAvatar;
    private String phone;
    private String country;
    private String subject;
    private String message;
    private String createdAt;
    private String updatedAt;
}
