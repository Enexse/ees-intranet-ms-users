package com.enexse.intranet.ms.users.models.partials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-cloudinary-docs")
@Builder
public class EesCloudinaryDoc {

    @Id
    private String cloudinaryId;

    private String eesUploadType;
    private String userId;
    private String signature;
    private String format;
    private String resourceType;
    private String secureUrl;
    private String assetId;
    private String versionId;
    private String type;
    private String version;
    private String url;
    private String publicId;
    private List<String> tags;
    private String pages;
    private String folder;
    private String originalFilename;
    private String apiKey;
    private String bytes;
    private String width;
    private String etag;
    private boolean placeholder;
    private String height;
    private String createdAt;
}
