package com.api.campuslink.controllers;

import com.api.campuslink.models.entities.Department;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PostMapping("/add")
    public ResponseEntity<?> insertDepartment(@RequestBody Department req) {
        log.info("Got request to save department");
        Department department = Department.builder()
                .code(req.getCode())
                .name(req.getName())
                .build();

        Result<Department> result = departmentService.insertDepartment(department);

        if (!result.isSuccess()) {
            log.error(result.getError());
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result.getData(), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDepartments(@RequestParam(defaultValue = "name") String sortBy,
                                               @RequestParam(defaultValue = "true") boolean ascending) {

        log.info("Got request to fetch all the departments");
        Result<List<Department>> result = this.departmentService.getAllDepartment(sortBy, ascending);

        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result.getData().isEmpty()) {
            log.info("No department available");
            return new ResponseEntity<>("No department available", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getData(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getDepartment(@RequestParam String findBy, @RequestParam String value) {
        log.info("Got request to fetch Department with " + findBy + ": " + value);
        Result<Department> result = this.departmentService.getDepartment(findBy, value);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result.getData() == null) {
            log.info("No data found for given " + findBy + " = " + value);
            return new ResponseEntity<>("No data found for given " + findBy + " = " + value, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getData(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDepartment(@RequestBody Department req) {
        log.info("Got request to update department with id = " + req.getId());
        Department department = Department.builder()
                .id(req.getId())
                .name(req.getName())
                .code(req.getCode())
                .build();
        Result<Department> result = this.departmentService.updateDepartment(department);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result.getData(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeDepartment(@RequestParam int id) {
        log.info("Got request to remove department with given id = " + id);
        Result<Department> result = this.departmentService.removeDepartment(id);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Department with given id = " + id + " removed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/multiple")
    public ResponseEntity<?> removeDepartments(@RequestParam String ids) {
        log.info("Got request to remove departments with given ids = " + ids);
        Result<Department> result = this.departmentService.removeDepartments(ids);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Departments with given ids = " + ids + "removed successfully", HttpStatus.OK);
    }
}
