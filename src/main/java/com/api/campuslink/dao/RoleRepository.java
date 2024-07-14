package com.api.campuslink.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.api.campuslink.entities.Role;


public interface RoleRepository extends CrudRepository<Role,Integer>{
    public List<Role>findAll(Sort sort);
    public Role findById(int id);
} 