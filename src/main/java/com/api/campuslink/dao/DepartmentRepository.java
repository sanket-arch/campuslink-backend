package com.api.campuslink.dao;

import com.api.campuslink.entities.Department;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department,Integer> {
    public List<Department> findAll(Sort sort);
    public Optional<Department> findByCode(String code);
    public Optional<Department> findByName(String name);
}