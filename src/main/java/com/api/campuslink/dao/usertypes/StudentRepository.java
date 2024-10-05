package com.api.campuslink.dao.usertypes;

import com.api.campuslink.models.entities.usertypes.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student,Long> {
    public List<Student> findAll(Sort sort);

    public Optional<Student> findByRegNo(String regNo);
    public Optional<Student> findByUserName(String userName);
}
