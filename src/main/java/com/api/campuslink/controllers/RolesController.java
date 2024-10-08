package com.api.campuslink.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.campuslink.models.entities.Role;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.RolesServices;

@RestController
@RequestMapping("/api/role")
public class RolesController {

    @Autowired
    RolesServices rolesServices;

    @GetMapping("/all")
    public ResponseEntity<?> getRoles(@RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc) {
        Result<List<Role>> response = this.rolesServices.getAllRoles(sortBy, asc);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getData().isEmpty()) {
            return new ResponseEntity<>("No data available ", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getRole(@RequestParam int id) {
        Result<Role> response = this.rolesServices.getRoleById(id);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getData() == null) {
            return new ResponseEntity<>("No data available for the given id", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createRole(@RequestBody Role req) {
        Role role = new Role(
                req.getId(),
                req.getName(),
                req.getRoleCode(),
                req.getDescription());

        Result<Role> response = this.rolesServices.insertNewRole(role);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Role saved successfully with id " + response.getData().getId(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to save due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRole(@RequestBody Role req) {

        Result<Role> response = this.rolesServices.updateRole(req);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Role updated successfully for id " + response.getData().getId(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to update due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeRole(@RequestParam int id) {

        Result<Role> response = this.rolesServices.deleteRole(id);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Role removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to remove due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/multiple")
    public ResponseEntity<?> removeRoles(@RequestParam String ids) {
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        Result<Role> response = this.rolesServices.deleteRoles(idList);

        if (response.isSuccess()) {
            return new ResponseEntity<>("Roles removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to remove due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
