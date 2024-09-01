package com.api.campuslink.controllers;

import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.UserService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ObjectNode req) {
        log.info("Got request for logging user in");
        Result<Object> result = this.userService.verify(req);

        if (!result.isSuccess()) {
            log.debug("Unable to verify the user");
            return new ResponseEntity<>(result.getError(), HttpStatus.UNAUTHORIZED);
        }
        log.info("User verified successfully");
        return ResponseEntity.ok(result.getData());
    }
}
