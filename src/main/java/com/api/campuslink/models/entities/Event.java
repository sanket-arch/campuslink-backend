package com.api.campuslink.models.entities;

import com.api.campuslink.models.entities.usertypes.Student;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String EventSubject;
    private String EventDescription;
    private String venue;
    private LocalDate eventStartTime;
    private LocalDate eventEndTime;
    @ManyToOne
    @JoinColumn(name = "campus_id")
    private Campus campus;
    @ManyToOne
    @JoinColumn(name = "eventCode", referencedColumnName = "eventCode", nullable = false)
    private EventType eventType;
    @ManyToOne
    @JoinColumn(name = "hostedBy", referencedColumnName = "clubCode", nullable = false)
    private Club hostedBy;

    @ManyToMany
    @JoinTable(name = "event_interested",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> interestedStudents;

}
