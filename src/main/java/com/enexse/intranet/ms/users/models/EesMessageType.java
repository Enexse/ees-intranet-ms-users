package com.enexse.intranet.ms.users.models;

import com.enexse.intranet.ms.users.enums.EesStatus;
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
@Document(collection = "ees-message-types")
@Builder
public class EesMessageType {

    @Id
    private String id;

    private String code;
    private EesStatus status;
    private String title;
    private String description;
    private String createdAt;
    private String updatedAt;

    @DBRef
    private EesUser createdBy;
}
