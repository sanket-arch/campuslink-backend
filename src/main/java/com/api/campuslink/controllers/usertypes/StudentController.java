package com.api.campuslink.controllers.usertypes;

import com.api.campuslink.models.entities.usertypes.Student;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.usertypes.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/student")
@Slf4j
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody Student req) {
        log.info("Got request to add new student");

        Student student = Student.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .userName(req.getUserName())
                .password(req.getPassword())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .profilePicture(req.getProfilePicture())
                .role(req.getRole())
                .campus(req.getCampus())
                .regNo(req.getRegNo())
                .course(req.getCourse())
                .passingYear(req.getPassingYear())
                .build();
        Result<Student> result = studentService.insertStudent(student);

        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Student saved successfully with registration number " + result.getData().getRegNo());
        return new ResponseEntity<>("Student saved successfully with registration number " + result.getData().getRegNo(), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllStudent(@RequestParam(defaultValue = "regNo") String sortBy,
                                           @RequestParam(defaultValue = "true") boolean asc) {
        log.info("Got request to fetch all the users");
        Result<List<Student>> response = this.studentService.getAllStudents(sortBy, asc);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getData().isEmpty()) {
            return new ResponseEntity<>("No student available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getStudent(@RequestParam String findBy, @RequestParam String value) {
        log.info("Got request to get student with " + findBy + " = " + value);
        Result<Student> response = this.studentService.getStudent(findBy, value);

        if (!response.isSuccess()) {
            log.info("Unable to fetch student with " + findBy + " = " + value);
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (response.getData() == null) {
            log.info("No student is found with " + findBy + " = " + value);
            return new ResponseEntity<>("No student is found with " + findBy + " = " + value, HttpStatus.NOT_FOUND);
        }

        log.info("Student found with " + findBy + " = " + value);
        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStudent(@RequestBody Student student) {
        log.info("Got request for updating user with id " + student.getUserId());
        Result<Student> response = this.studentService.updateStudent(student);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeStudent(@RequestParam Long id) {
        log.info("Got request to delete user with id = " + id);
        Result<Student> response = this.studentService.removeStudent(id);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Student with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/delete/multiple")
    public ResponseEntity<?> removeStudents(@RequestParam String ids) {
        Result<Student> response = this.studentService.removeStudents(ids);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
