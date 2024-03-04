package com.enexse.intranet.ms.users.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Document(collection = "ees-user-subRequests")
@Builder
public class EesUserSubRequest {

    @Id
    private String subRequestId;

    private String subRequestCode;
    private String description;

    @DBRef(lazy = true)
    @JsonIgnoreProperties("subRequests") // ignore this field when serializing to JSON
    private EesUserRequest eesUserRequest;

    private String createdAt;
    private String updatedAt;
    private EesUser createdBy;
}
