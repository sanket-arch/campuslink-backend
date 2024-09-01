package com.api.campuslink.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.campuslink.dao.CourseRepository;
import com.api.campuslink.entities.Course;
import com.api.campuslink.helpers.Result;

@Service
public class CourseServices {
    @Autowired
    CourseRepository courseRepository;

    public Result<Course> insertCourse(Course course) {
        try {
            if (this.courseRepository.existsById(course.getId())) {
                throw new DataIntegrityViolationException("Course with given id already exists");
            }

            Course courseSaved = this.courseRepository.save(course);
            return Result.success(courseSaved);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<List<Course>> fetchAllCourses(String sortBy, boolean ascending) {

        try {
            List<Course> courseList = new ArrayList<Course>();
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            courseList = this.courseRepository.findAll(sort);
            return Result.success(courseList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Course> fetchCourseById(int id) {
        try {
            Course courseFound = this.courseRepository.findById(id);

            return Result.success(courseFound);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Course> updateCourse(Course course) {
        try {
            if (!this.courseRepository.existsById(course.getId())) {
                throw new DataIntegrityViolationException("Course with given id does not exists");
            }

            Course courseUpdated = this.courseRepository.save(course);
            return Result.success(courseUpdated);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Course> deleteCourse(int id) {
        try {
            if (!this.courseRepository.existsById(id)) {
                throw new DataIntegrityViolationException("Course with given id does not exists");
            }
            this.courseRepository.deleteById(id);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Course> deleteCourses(List<Integer> ids) {
        try {
            this.courseRepository.deleteAllById(ids);
            ;
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
