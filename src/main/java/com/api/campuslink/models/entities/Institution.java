package com.api.campuslink.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Institution")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(InstitutionId.class)
public class Institution {

    @Id
    @Column(name = "BANK_CODE")
    private String bankCode;

    @Id
    @Column(name = "INSTITUTE_CODE")
    private String instituteCode;

    @Column(name = "INSTITUTION_NAME")
    private String name;

    @OneToMany(mappedBy = "institution")
    private List<Device> devices;
}
