package com.api.campuslink.models.entities.usertypes;

import com.api.campuslink.models.entities.Course;
import com.api.campuslink.models.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"regNo"})
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends User {

    @NotEmpty(message = "Registration number cannot be empty or null")
    private String regNo;
    @NotNull(message = "PassingYear cannot be null")
    private int passingYear;

    @NotNull(message = "Course cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

}
