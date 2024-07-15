package com.api.campuslink.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.api.campuslink.entities.Campus;

public interface CampusRepository extends CrudRepository<Campus,Integer>{
    public List<Campus> findAll(Sort sort);
    public Campus findById(int id);
}
