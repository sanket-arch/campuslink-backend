package com.api.campuslink.models.entities;

import com.api.campuslink.models.entities.usertypes.Faculty;
import com.api.campuslink.models.entities.usertypes.Student;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String type;
    private LocalDate foundedDate;
    private String logo;

    private Integer totalMembers;
    private String eligibilityCriteria;
    private Double membershipFee;

    @ElementCollection
    private List<String> socialLinks;

    @OneToOne
    private Faculty facultyAdvisor;

    @ManyToMany
    private List<Student> members;

    @ManyToOne
    private Campus campus;

    @OneToOne
    private Student president;

    @OneToOne
    private Student vicePresident;

    @OneToMany
    private List<Event> upcomingEvents;

    @OneToMany
    private List<Event> pastEvents;

}

