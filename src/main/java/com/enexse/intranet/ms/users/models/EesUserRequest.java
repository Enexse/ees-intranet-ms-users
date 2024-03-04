package com.enexse.intranet.ms.users.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-user-requests")
@Builder
public class EesUserRequest {

    @Id
    private String requestId;

    private String requestCode;
    private String requestTitle;

    @DBRef(lazy = true)
    @JsonIgnoreProperties("eesUserRequest") // ignore this field when serializing to JSON
    private List<EesUserSubRequest> subRequests = new ArrayList<>();

    private String createdAt;
    private String updatedAt;
    private EesUser createdBy;
}
