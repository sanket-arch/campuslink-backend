package com.api.campuslink.controllers.usertypes;

import com.api.campuslink.models.entities.usertypes.Faculty;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.usertypes.FacultyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/faculty")
@Slf4j
public class FacultyController {
    @Autowired
    FacultyService facultyService;

    @PostMapping("/add")
    public ResponseEntity<?> insertFaculty(@RequestBody Faculty req) {
        log.info("Got request to insert new faculty");
        Faculty faculty = Faculty.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .userName(req.getUserName())
                .password(req.getPassword())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .profilePicture(req.getProfilePicture())
                .role(req.getRole())
                .campus(req.getCampus())
                .facultyCode(req.getFacultyCode())
                .officeLocation(req.getOfficeLocation())
                .course(req.getCourse())
                .department(req.getDepartment())
                .build();

        Result<Faculty> result = this.facultyService.insertFaculty(faculty);

        if (!result.isSuccess()) {
            log.debug(result.getError());
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result.getData(), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFaculty(@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "true") boolean ascending) {
        log.info("Got request to fetch all the faculties");
        Result<List<Faculty>> result = this.facultyService.getAllFaculty(sortBy, ascending);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result.getData().isEmpty()) {
            return new ResponseEntity<>("Data not available.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getData(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getFaculty(@RequestParam(defaultValue = "id") String prop, @RequestParam String value) {
        log.info("Got request to fetch faculty with " + prop + " = " + value);
        Result<Faculty> result = this.facultyService.getFaculty(prop, value);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result.getData() == null) {
            return new ResponseEntity<>("Faculty with " + prop + " = " + "is not available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getData(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateFaculty(@RequestBody Faculty req) {
        log.info("Got request to update faculty details");
        Result<Faculty> result = this.facultyService.updateFaculty(req);

        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result.getData(), HttpStatus.OK);
    }


}
