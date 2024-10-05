package com.api.campuslink.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.campuslink.dao.CampusRepository;
import com.api.campuslink.models.entities.Campus;
import com.api.campuslink.helpers.Result;

@Service
public class CampusServices {
    @Autowired
    CampusRepository campusRepository;

    public Result<Campus> insertCampus(Campus campus) {
        try {
            if (this.campusRepository.existsById(campus.getId())) {
                throw new DataIntegrityViolationException("Campus with given id already exists");
            }

            Campus campusSaved = this.campusRepository.save(campus);
            return Result.success(campusSaved);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<List<Campus>> fetchAllCampus(String sortBy, boolean ascending) {

        try {
            List<Campus> campusList = new ArrayList<Campus>();
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            campusList = this.campusRepository.findAll(sort);
            return Result.success(campusList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Campus> fetchCampusById(int id) {
        try {
            Campus campusFound = this.campusRepository.findById(id);

            return Result.success(campusFound);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Campus> updateCampus(Campus campus) {
        try {
            if (!this.campusRepository.existsById(campus.getId())) {
                throw new DataIntegrityViolationException("Campus with given id does not exists");
            }

            Campus campusUpdated = this.campusRepository.save(campus);
            return Result.success(campusUpdated);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Campus> deleteCampus(int id) {
        try {
            if (!this.campusRepository.existsById(id)) {
                throw new DataIntegrityViolationException("Campus with given id does not exists");
            }
            this.campusRepository.deleteById(id);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Campus> deleteCampuses(List<Integer> ids) {
        try {
            this.campusRepository.deleteAllById(ids);
            ;
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
