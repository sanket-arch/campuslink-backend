package com.api.campuslink.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.api.campuslink.entities.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    public List<Course> findAll(Sort sort);

    public Course findById(int id);
}
