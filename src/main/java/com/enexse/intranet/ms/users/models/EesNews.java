package com.enexse.intranet.ms.users.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Document(collection = "ees-news")
@Builder
public class EesNews {

    @Id
    private String newsId;

    private String newsTitle;
    private String newsDescription;
    private boolean newsStatus;
    private String createdAt;
    private String updatedAt;

    @DBRef
    @JsonManagedReference
    private EesUser createdBy;
}
