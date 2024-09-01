package com.api.campuslink.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Campuses")
@Data
@NoArgsConstructor
public class Campus {
    @Id
    private int id;
    private String campusName;
    private String campusDescription;
    private String campusAddress;
    private String link;


    public Campus(int id, String campusName, String campusDescription, String campusAddress, String link) {
        this.id = id;
        this.campusName = campusName;
        this.campusDescription = campusDescription;
        this.campusAddress = campusAddress;
        this.link = link;
    }

}
