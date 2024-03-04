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
@Document(collection = "ees-user-address")
public class EesUserAddress {

    @Id
    private String addressId;

    @Builder.Default
    private String country = "France";

    private String state;
    private String addressLine;
    private String zipCode;
}
