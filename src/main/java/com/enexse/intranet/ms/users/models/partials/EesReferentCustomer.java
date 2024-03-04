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
@Document(collection = "ees-client-referents")
@Builder
public class EesReferentCustomer {

    @Id
    private String referentId;

    private String firstName;
    private String lastName;
    private String profession;
    private String email;
    private List<String> phoneNumbers;
    private String createdAt;
    private String updatedAt;
}
