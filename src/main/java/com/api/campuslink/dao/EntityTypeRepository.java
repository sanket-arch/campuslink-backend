package com.api.campuslink.dao;

import com.api.campuslink.models.entities.EventType;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntityTypeRepository extends CrudRepository<EventType, Long> {

    public List<EventType> findAll(Sort sort);

}
