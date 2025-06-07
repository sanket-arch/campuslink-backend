package com.api.campuslink.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Event_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"eventCode"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventCode;
    private String name;
    private String description;

}
