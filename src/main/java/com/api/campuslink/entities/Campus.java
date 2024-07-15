package com.api.campuslink.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Campuses")
public class Campus {
    @Id
    private int id;
    private String campusName;
    private String campusDescription;
    private String campusAddress;
    private String link;

    public Campus() {
    }

    public Campus(int id, String campusName, String campusDescription, String campusAddress, String link) {
        this.id = id;
        this.campusName = campusName;
        this.campusDescription = campusDescription;
        this.campusAddress = campusAddress;
        this.link = link;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCampusName() {
        return this.campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusDescription() {
        return this.campusDescription;
    }

    public void setCampusDescription(String campusDescription) {
        this.campusDescription = campusDescription;
    }

    public String getCampusAddress() {
        return this.campusAddress;
    }

    public void setCampusAddress(String campusAddress) {
        this.campusAddress = campusAddress;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", campusName='" + getCampusName() + "'" +
                ", campusDescription='" + getCampusDescription() + "'" +
                ", campusAddress='" + getCampusAddress() + "'" +
                ", link='" + getLink() + "'" +
                "}";
    }

}
