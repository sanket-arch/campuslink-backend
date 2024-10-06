package com.api.campuslink.controllers;

import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.UserService;
import com.api.campuslink.services.security.JwtService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    JwtService jwtService;

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        log.info("Got request to log out the user");
        String token = this.jwtService.getTokenFromRequest(request);
        String username = null;
        if (token != null) {
            username = this.jwtService.getUsername(token);
            log.info(username + " logged out successfully");
            this.jwtService.blacklistToken(token);
            return new ResponseEntity<>(username + " logged out successfully", HttpStatus.OK);
        }
        log.info("Failed to log out user");
        return new ResponseEntity<>("Unable to log out user", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
