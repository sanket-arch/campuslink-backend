package com.api.campuslink.entities.usertypes;

import com.api.campuslink.entities.Campus;
import com.api.campuslink.entities.Course;
import com.api.campuslink.entities.Role;
import com.api.campuslink.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"regNo"})
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Student extends User {

    @NotEmpty(message = "Registration number cannot be empty or null")
    private String regNo;
    @NotNull(message = "PassingYear cannot be null")
    private int passingYear;

    @NotNull(message = "Course cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;


    public Student(String firstName, String lastName, String userName, String password, long phoneNumber, String email, byte[] profilePicture, Role role, Campus campus, String regNo, int passingYear, Course course) {
        super(firstName, lastName, userName, password, phoneNumber, email, profilePicture, role, campus);
        this.regNo = regNo;
        this.passingYear = passingYear;
        this.course = course;
    }
}
