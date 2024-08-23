package com.api.campuslink.services;

import com.api.campuslink.dao.RoleRepository;
import com.api.campuslink.dao.UserRespository;
import com.api.campuslink.entities.User;
import com.api.campuslink.helpers.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRespository userRespository;

    @Autowired
    RoleRepository roleRepository;

    String errorMsg = "";

    public Result<User> insertUser(User user) {
        try {
            if (userRespository.existsById(user.getUserId())) {
                throw new DataIntegrityViolationException("This user already exists");
            }
            if (!roleRepository.existsById(user.getRole().getId())) {
                return Result.error("specified role does not exist");
            }
            User savedUser = userRespository.save(user);
            return Result.success(savedUser);
        } catch (ConstraintViolationException e) {
            errorMsg = e.getConstraintViolations().stream()
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
