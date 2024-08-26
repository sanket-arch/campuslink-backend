package com.api.campuslink.controllers;

import com.api.campuslink.entities.User;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/add/user")
    public ResponseEntity<?> addUser(@RequestBody User req) {
        User user = new User(req.getFirstName(), req.getLastName(), req.getUserName(), req.getPassword(), req.getPhoneNumber(), req.getEmail(), req.getProfilePicture(), req.getRole(), req.getCampus());

        Result<User> response = this.userService.insertUser(user);

        if (response.isSuccess()) {
            return new ResponseEntity<>("User saved successfully with id " + response.getData().getUserId(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to save due to " + response.getError(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/users")
    public ResponseEntity<?> getALlUsers(@RequestParam(defaultValue = "userId") String sortBy,
                                         @RequestParam(defaultValue = "true") boolean asc) {

        Result<List<User>> result = this.userService.getAllUser(sortBy, asc);

        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result.getData().isEmpty()) {
            return new ResponseEntity<>("No user available", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result.getData(), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserById(@RequestParam long userId) {

        Result<User> response = this.userService.getUserbyId(userId);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (response.getData() == null) {
            return new ResponseEntity<>("No data available for user id " + userId, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);

    }

    @PutMapping("/update/user")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Result<User> response = this.userService.updateUser(user);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<?> removeUser(@RequestParam long id) {
        Result<User> response = this.userService.deleteUserById(id);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("User with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/delete/users")
    public ResponseEntity<?> removeusers(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Result<User> response = this.userService.deleteUsers(idList);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(response.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("users with ids " + ids + " removed succesfully", HttpStatus.OK);

    }
}