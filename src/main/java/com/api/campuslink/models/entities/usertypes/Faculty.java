package com.api.campuslink.models.entities.usertypes;

import com.api.campuslink.models.entities.Course;
import com.api.campuslink.models.entities.Department;
import com.api.campuslink.models.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "faculties", uniqueConstraints = {
        @UniqueConstraint(columnNames = "facultyCode")
})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Faculty extends User {
    @NotEmpty(message = "Faculty code cannot be empty or null")
    private String facultyCode;

    @NotEmpty(message = "Office location of faulty cannot be null or empty")
    private String officeLocation;

    @NotNull(message = "Course cannot be empty or null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull(message = "Department cannot be empty")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;

}


