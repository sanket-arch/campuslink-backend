package com.api.campuslink.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.campuslink.entities.Campus;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.CampusServices;

@RestController
public class CampusController {
    @Autowired
    CampusServices campusServices;

    @PostMapping("/add/campus")
    public ResponseEntity<?> createCampus(@RequestBody Campus req) {

        Campus campus = new Campus(
                req.getId(),
                req.getCampusName(),
                req.getCampusDescription(),
                req.getCampusAddress(),
                req.getLink());

        Result<Campus> response = this.campusServices.insertCampus(campus);

        if (!response.isSuccess()) {
            return new ResponseEntity<>("Unable to save due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Campus saved successfully with id " + response.getData().getId(),
                HttpStatus.OK);
    }

    @GetMapping("/campuses")
    public ResponseEntity<?> getCampuses(@RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc) {
        Result<List<Campus>> response = this.campusServices.fetchAllCampus(sortBy, asc);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getData().isEmpty()) {
            return new ResponseEntity<>("No data available ", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @GetMapping("/campus")
    public ResponseEntity<?> getCampus(@RequestParam int id) {
        Result<Campus> response = this.campusServices.fetchCampusById(id);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getData() == null) {
            return new ResponseEntity<>("No data available for the given id", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @PutMapping("/update/campus")
    public ResponseEntity<?> updateCampus(@RequestBody Campus req) {

        Result<Campus> response = this.campusServices.updateCampus(req);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Campus updated successfully for id " + response.getData().getId(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to update due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove/campus")
    public ResponseEntity<?> removeCampus(@RequestParam int id) {
        Result<Campus> response = this.campusServices.deleteCampus(id);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Campus removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to remove due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove/campuses")
    public ResponseEntity<?> removeCampuses(@RequestParam String ids) {
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Result<Campus> response = this.campusServices.deleteCampuses(idList);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Campuses removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to remove due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
