package com.api.campuslink.services;

import com.api.campuslink.dao.RoleRepository;
import com.api.campuslink.dao.UserRespository;
import com.api.campuslink.entities.User;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.services.security.JwtService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    @Autowired
    UserRespository userRespository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    public Result<Object> verify(ObjectNode credentials) {
        try {
            log.info("Got request to verify the user");
            String username = credentials.get("username").asText();
            String password = credentials.get("password").asText();

            // Using authenticationManager from the SecurityConfig class
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (!authentication.isAuthenticated()) {
                log.debug("Unable to verify the user");
                return Result.error("Unable to verify the user");
            }
            log.info("User validated successfully");

            log.info("Generating token");
            String jwtToken = this.jwtService.generateToken(username);
            log.info("Token generated successfully");

            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("Token",jwtToken);
            responseMap.put("Message","Token is valid for 30 min");

            return Result.success(responseMap);
        } catch (Exception e) {
            log.info("Unable to verify the user");
            log.info(e.getMessage());
            return Result.error(e.getMessage());
        }

    }

    public Result<User> insertUser(User user) {
        try {
            if (userRespository.existsById(user.getUserId())) {
                throw new DataIntegrityViolationException("This user already exists");
            }
            if (!roleRepository.existsById(user.getRole().getId())) {
                return Result.error("specified role does not exist");
            }
            user.setPassword(encoder.encode(user.getPassword()));
            User savedUser = userRespository.save(user);
            return Result.success(savedUser);
        } catch (ConstraintViolationException e) {
            String errorMsg = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            return Result.error(errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<List<User>> getAllUser(String sortBy, boolean ascending) {
        try {
            List<User> userList = new ArrayList<User>();
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            userList = userRespository.findAll(sort);
            return Result.success(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    public Result<User> getUserbyId(long userId) {
        try {
            Optional<User> userOptional = this.userRespository.findById(userId);
            return userOptional.map(Result::success).orElseGet(() -> Result.success(null));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    public Result<User> updateUser(User user) {
        try {
            long id = user.getUserId();

            if (!this.userRespository.existsById(id)) {
                return Result.error("The user with given id " + id + " does not exist.");
            }
            if (!roleRepository.existsById(user.getRole().getId())) {
                return Result.error("Specified role does not exist");
            }
            User updatedUser = this.userRespository.save(user);
            return Result.success(updatedUser);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<User> deleteUserById(long id) {
        try {
            if (!userRespository.existsById(id)) {
                throw new Exception("User with " + id + " does not exists");
            }

            userRespository.deleteById(id);
            return Result.success(null);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<User> deleteUsers(List<Long> ids) {
        try {
            this.userRespository.deleteAllById(ids);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
