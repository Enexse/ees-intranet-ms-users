package com.enexse.intranet.ms.users.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ees-departments")
@Builder
public class EesUserDepartment {

    @Id
    private String departmentId;

    private String departmentCode;
    private String departmentDescription;

    private List<String> professions = new ArrayList<>();
    private HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
    private String defaultColor;
    private String createdAt;
    private String updatedAt;

    @ToString.Exclude
    @DBRef
    @JsonManagedReference
    private EesUser createdBy;
}
