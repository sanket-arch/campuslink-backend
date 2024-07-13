package com.api.campuslink.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    private int id;

    private String CourseName;

    @Column(unique = true)
    private String CourseCode;
    private String description;


    public Course() {
    }

    public Course(int id, String CourseName, String CourseCode, String description) {
        this.id = id;
        this.CourseName = CourseName;
        this.CourseCode = CourseCode;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return this.CourseName;
    }

    public void setCourseName(String CourseName) {
        this.CourseName = CourseName;
    }

    public String getCourseCode() {
        return this.CourseCode;
    }

    public void setCourseCode(String CourseCode) {
        this.CourseCode = CourseCode;
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
            ", CourseName='" + getCourseName() + "'" +
            ", CourseCode='" + getCourseCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }


}
