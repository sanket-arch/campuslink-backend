package com.api.campuslink.models.entities;

import com.api.campuslink.models.entities.usertypes.Faculty;
import com.api.campuslink.models.entities.usertypes.Student;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "club", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"clubCode"})
})
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clubCode;
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
    @JoinTable(name = "club_members",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Student> members;

    @ManyToOne
    @JoinColumn(name = "campus_id")
    private Campus campus;

    @OneToOne
    @JoinColumn(name = "president", referencedColumnName = "regNo", nullable = false)
    private Student president;

    @OneToOne
    @JoinColumn(name = "vicePresident", referencedColumnName = "regNo", nullable = false)
    private Student vicePresident;

    @OneToMany(mappedBy = "hostedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;


}

