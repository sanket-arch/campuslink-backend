package com.api.campuslink.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.campuslink.entities.Course;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.CourseServices;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseServices courseServices;

    @PostMapping("/add")
    public ResponseEntity<?> createCourse(@RequestBody Course req) {

        Course course = new Course(
                req.getId(),
                req.getCourseName(),
                req.getCourseCode(),
                req.getDescription());

        Result<Course> response = this.courseServices.insertCourse(course);

        if (!response.isSuccess()) {
            return new ResponseEntity<>("Unable to save due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Course saved successfully with id " + response.getData().getId(),
                HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getCourses(@RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc) {
        Result<List<Course>> response = this.courseServices.fetchAllCourses(sortBy, asc);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getData().isEmpty()) {
            return new ResponseEntity<>("No data available ", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getCourse(@RequestParam int id) {
        Result<Course> response = this.courseServices.fetchCourseById(id);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getData() == null) {
            return new ResponseEntity<>("No data available for the given id", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCourse(@RequestBody Course req) {

        Result<Course> response = this.courseServices.updateCourse(req);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Course updated successfully for id " + response.getData().getId(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to update due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeCourse(@RequestParam int id) {
        Result<Course> response = this.courseServices.deleteCourse(id);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Course removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to remove due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/multiple")
    public ResponseEntity<?> removeCourses(@RequestParam String ids) {
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Result<Course> response = this.courseServices.deleteCourses(idList);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Courses removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to remove due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
