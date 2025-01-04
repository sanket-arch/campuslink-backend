package com.api.campuslink.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"activity_code"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String activity_code;
    private String name;
    private String description;

}
