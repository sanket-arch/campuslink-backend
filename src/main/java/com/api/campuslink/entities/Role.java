package com.api.campuslink.entities;


import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    private int id;
    private String name;
    
    @Column(unique = true)
    private String roleCode;
    private String description;

    public Role(int id, String name, String roleCode, String description) {
        this.id = id;
        this.name = name;
        this.roleCode = roleCode;
        this.description = description;
    }

}
