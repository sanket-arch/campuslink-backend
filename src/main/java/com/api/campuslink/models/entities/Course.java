package com.api.campuslink.models.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
public class Course {

    @Id
    private int id;

    private String courseName;

    @Column(unique = true)
    private String courseCode;
    private String description;

    public Course(int id, String courseName, String courseCode, String description) {
        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
    }
}
