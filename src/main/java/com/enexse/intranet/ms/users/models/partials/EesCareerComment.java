package com.enexse.intranet.ms.users.models.partials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-career-comments")
@Builder
public class EesCareerComment {

    @Id
    private String commentId;

    private String text;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
}
