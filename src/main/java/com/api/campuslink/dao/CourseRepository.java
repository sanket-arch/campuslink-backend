package com.api.campuslink.dao;

import org.springframework.data.repository.CrudRepository;

import com.api.campuslink.entities.Course;

public interface CourseRepository extends CrudRepository<Course,Integer>{
    
}
