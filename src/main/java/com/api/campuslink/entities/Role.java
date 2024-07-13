package com.api.campuslink.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    private int id;
    private String name;
    
    @Column(unique = true)
    private String roleCode;
    private String description;

    public Role() {
    }

    public Role(int id, String name, String roleCode, String description) {
        this.id = id;
        this.name = name;
        this.roleCode = roleCode;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", roleCode='" + getRoleCode() + "'" +
                ", description='" + getDescription() + "'" +
                "}";
    }

}
