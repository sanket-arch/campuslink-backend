package com.api.campuslink.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity(name = "departments")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "code"})
})
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Department name cannot be empty ot null")
    private String name;

    @NotEmpty(message = "Department code cannot be empty")
    private String code;

}
