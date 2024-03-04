package com.enexse.intranet.ms.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-order-numbers")
@Builder
public class EesOrderNumber {

    @Id
    private String id;

    private String code;

    @DBRef
    private EesCustomer customer;

    private String from;
    private String to;

    private String createdAt;
    private String updatedAt;

    @DBRef
    private EesUser createdBy;
}
