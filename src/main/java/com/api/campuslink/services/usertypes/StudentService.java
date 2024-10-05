package com.api.campuslink.services.usertypes;

import com.api.campuslink.dao.CampusRepository;
import com.api.campuslink.dao.CourseRepository;
import com.api.campuslink.dao.RoleRepository;
import com.api.campuslink.dao.usertypes.StudentRepository;
import com.api.campuslink.models.entities.Campus;
import com.api.campuslink.models.entities.Course;
import com.api.campuslink.models.entities.Role;
import com.api.campuslink.models.entities.usertypes.Student;
import com.api.campuslink.helpers.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CampusRepository campusRepository;

    public Result<Student> insertStudent(Student student) {
        try {
            log.info("Saving new student with registration number " + student.getRegNo());
            Result<Student> studentDetails = this.getStudentOtherDetail(student);

            if (!studentDetails.isSuccess()) {
                return studentDetails;
            }
            student = studentDetails.getData();
            student.setPassword(encoder.encode(student.getPassword()));
            Student savedStudent = studentRepository.save(student);
            log.info("Student with registration number " + savedStudent.getRegNo() + " saved successfully with user id " + savedStudent.getUserId());
            return Result.success(savedStudent);
        } catch (ConstraintViolationException e) {
            log.debug("Got error while saving the student");
            String errMsg = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            log.error(errMsg);
            return Result.error(errMsg);
        } catch (Exception e) {
            log.debug("Got error while saving the student");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<List<Student>> getAllStudents(String sortBy, boolean ascending) {
        try {
            log.info("Fetching all students");
            List<Student> studentList;

            if (sortBy.equals("regNo")) {
                studentList = (List<Student>) this.studentRepository.findAll();

                studentList.sort(new Comparator<Student>() {
                    @Override
                    public int compare(Student s1, Student s2) {
                        int order;
                        String reg1 = s1.getRegNo();
                        String reg2 = s2.getRegNo();

                        // Extract the year, course, and numeric parts from the reg number
                        int year1 = Integer.parseInt(reg1.substring(0, 2));
                        int year2 = Integer.parseInt(reg2.substring(0, 2));

                        String course1 = reg1.substring(2, 5);
                        String course2 = reg2.substring(2, 5);

                        int seq1 = Integer.parseInt(reg1.substring(5));
                        int seq2 = Integer.parseInt(reg2.substring(5));

                        // First compare the year
                        if (year1 != year2) {
                            order = year1 - year2;
                            return ascending ? order : -order;
                        }

                        // If years are the same, compare the course
                        int courseComparison = course1.compareTo(course2);
                        if (courseComparison != 0) {
                            order = courseComparison;
                            return ascending ? order : -order;
                        }

                        // If course is also the same, compare the numeric part
                        order = seq1 - seq2;
                        return ascending ? order : -order;
                    }
                });
            } else {
                Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
                studentList = studentRepository.findAll(sort);
            }

            log.info("Successfully fetched all the students");
            return Result.success(studentList);
        } catch (Exception e) {
            log.info("Got error while fetching the students");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<Student> getStudent(String prop, String value) {
        try {
            log.info("fetching student with " + prop + " = " + value);

            Optional<Student> studentOptional = switch (prop) {
                case "id" -> this.studentRepository.findById(Long.parseLong(value));
                case "regNo" -> this.studentRepository.findByRegNo(value);
                case "userName" -> this.studentRepository.findByUserName(value);
                default -> Optional.empty();
            };

            return studentOptional.map(Result::success).orElseGet(() -> Result.success(null));

        } catch (Exception e) {
            log.debug("Something went wrong while fetching student");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<Student> updateStudent(Student student) {
        try {
            log.info("Updating Student with id " + student.getUserId());
            Long id = student.getUserId();
            if (!this.studentRepository.existsById(id)) {
                log.debug("user with given id does not exist");
                return Result.error("user with given id does not exist");
            }

            Result<Student> studentDetails = this.getStudentOtherDetail(student);
            if (!studentDetails.isSuccess()) {
                return studentDetails;
            }

            student = studentDetails.getData();
            Student updatedStudent = this.studentRepository.save(student);
            log.info("successfully updated student with id " + updatedStudent.getUserId());
            return Result.success(updatedStudent);
        } catch (Exception e) {
            log.debug("Got error while updating user with id " + student.getUserId());
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<Student> removeStudent(Long userId) {
        try {
            log.info("Removing student with id " + userId);
            if (!this.studentRepository.existsById(userId)) {
                log.debug("No student with given id exist");
                return Result.error("Student with id = " + userId + " does not exist");
            }
            this.studentRepository.deleteById(userId);
            log.info("Student with id = " + userId + " removed successfully");
            return Result.success(null);
        } catch (Exception e) {
            log.debug("Got error while removing student");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<Student> removeStudents(String ids) {
        try {
            log.info("Removing students with ids " + ids);
            List<Long> idList = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .toList();
            this.studentRepository.deleteAllById(idList);
            log.info("Students Removed successfully");
            return Result.success(null);
        } catch (Exception e) {
            log.debug("Something went wrong while deleting th ids");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }

    }

    private Result<Student> getStudentOtherDetail(@NotNull Student student) {

        int courseID = student.getCourse().getId();
        int roleID = student.getRole().getId();
        int campusID = student.getCampus().getId();

        if (!courseRepository.existsById(courseID)) {
            log.debug("Course with id " + courseID + " does not exist");
            return Result.error("Course with id " + courseID + " does not exist");
        }
        if (!roleRepository.existsById(roleID)) {
            log.debug("Role with id " + roleID + "does not exist");
            return Result.error("Role with id " + roleID + " does not exist");
        }
        if (!campusRepository.existsById(campusID)) {
            log.debug("Campus with id " + campusID + "does not exist");
            return Result.error("Campus with id " + campusID + " does not exist");
        }

        Course course = courseRepository.findById(courseID);
        student.setCourse(course);
        Role role = roleRepository.findById(roleID);
        student.setRole(role);
        Campus campus = campusRepository.findById(campusID);
        student.setCampus(campus);

        return Result.success(student);
    }

}
