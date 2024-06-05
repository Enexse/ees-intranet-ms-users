package com.enexse.intranet.ms.users.models;

import com.enexse.intranet.ms.users.enums.EesStatusRequest;
import com.enexse.intranet.ms.users.models.partials.EesCloudinaryDoc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-create-requests")
@Builder
public class EesUserCreateRequest {

    @Id
    private String id;

    public String type;
    private EesUserRequest request;
    private EesUserSubRequest subRequest;
    private EesUser recipient;
    private EesUser referent;
    private String comments;
    private String decline;

    @DBRef
    private EesUser user;

    private String personalEmail;
    private String enexseEmail;
    private String password;
    private EesStatusRequest status;
    private String createdAt;
    private String updatedAt;
    //private HashMap<String, String> attachmentFileIds;

    @DBRef
    private List<EesCloudinaryDoc> attachments;
}
