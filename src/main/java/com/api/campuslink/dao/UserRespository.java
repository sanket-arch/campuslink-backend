package com.api.campuslink.dao;

import com.api.campuslink.models.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRespository extends CrudRepository<User, Long> {
    public List<User> findAll(Sort sort);

    public User findByUserName(String userName);
}
