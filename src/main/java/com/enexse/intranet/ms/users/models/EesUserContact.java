package com.enexse.intranet.ms.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-user-contacts")
public class EesUserContact {

    @Id
    private String contactId;

    private String prefixPhone;
    private String phoneNumber;
}
