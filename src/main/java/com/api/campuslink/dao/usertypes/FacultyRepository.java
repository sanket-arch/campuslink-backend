package com.api.campuslink.dao.usertypes;

import com.api.campuslink.models.entities.usertypes.Faculty;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends CrudRepository<Faculty, Long> {
    public List<Faculty> findAll(Sort sort);
    public Optional<Faculty> findByFacultyCode(String facultyCode);
    public Optional<Faculty> findByUserName(String username);
    public Optional<Faculty> findByUserId(Long userId);
}
