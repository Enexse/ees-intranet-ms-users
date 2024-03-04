package com.enexse.intranet.ms.users.models;

import com.enexse.intranet.ms.users.enums.EesStatusUser;
import com.enexse.intranet.ms.users.models.partials.EesGenerator;
import com.enexse.intranet.ms.users.models.partials.EesUserInfo;
import com.enexse.intranet.ms.users.models.timesheet.EesUserTimesheet;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-users")
@Builder
public class EesUser {

    @Id
    @GeneratedValue(generator = EesGenerator.generatorName)
    @GenericGenerator(name = EesGenerator.generatorName, strategy = "com.enexse.intranet.ms.users.models.partials.EesGenerator")
    private String userId;

    private String keycloakId;
    private String firstName;
    private String lastName;
    private String pseudo;

    private String personalEmail;
    private String enexseEmail;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private EesStatusUser status;
    private String gender;
    private String notes;

    @DBRef(lazy = true)
    @JsonManagedReference
    private EesUserRole role;

    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String matricule;
    private int experience;

    @DBRef
    private EesUserAddress userAddress;

    @DBRef
    private EesUserContact userContact;

    @ToString.Exclude
    @DBRef(lazy = true)
    @JsonBackReference
    private EesUserEntity eesEntity;
    private String entityCode;

    @ToString.Exclude
    @DBRef
    @JsonBackReference
    private EesUserDepartment eesDepartment;
    private String departmentCode;

    @ToString.Exclude
    @DBRef(lazy = true)
    @JsonBackReference
    private EesUserProfession eesProfession;
    private String professionCode;

    @DBRef(lazy = true)
    @JsonManagedReference
    private EesUserTimesheet eesUserTimesheet;

    @DBRef
    private EesUserInfo eesUserInfo;

    @DBRef(lazy = true)
    @JsonIgnoreProperties("customer")
    private List<EesMission> missions;

    @DBRef
    private List<EesFormation> formations;
}
