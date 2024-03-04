package com.enexse.intranet.ms.users.models;


import com.enexse.intranet.ms.users.models.partials.EesGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-formations")
@Builder
public class EesFormation {

    @Id
    @GeneratedValue(generator = EesGenerator.generatorName)
    private String formationId ;

    private String formationName;
    private String organisme;
    private String certification;
    private String startDate;
    private String endDate;
}
