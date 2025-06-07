package com.api.campuslink.services;

import com.api.campuslink.dao.CampusRepository;
import com.api.campuslink.dao.RoleRepository;
import com.api.campuslink.dao.UserRepository;
import com.api.campuslink.exceptions.InternalProcessingException;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.UserDTO;
import com.api.campuslink.models.entities.Campus;
import com.api.campuslink.models.entities.Role;
import com.api.campuslink.models.entities.User;
import com.api.campuslink.services.security.JwtService;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    @Autowired
    private UserRepository userRepository;

    @Autowired
   private RoleRepository roleRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    public Result<Object> verify(ObjectNode credentials) {
        try {
            log.info("Got request to verify the user");
            String username = credentials.get("username").asText();
            String password = credentials.get("password").asText();

            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                log.debug("Username or password is empty");
                return Result.error("Username or password is empty");
            }
            log.info("Validating user credentials");

            User userDetails = userRepository.findByUserName(username);

            if(userDetails == null) {
                log.error("User with username {} does not exist", username);
                return Result.error("User with username " + username + " does not exist");
            }

            log.info("User with username {} exists, proceeding to authentication", username);

            // Using authenticationManager from the SecurityConfig class
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (!authentication.isAuthenticated()) {
                log.debug("Unable to verify the user");
                return Result.error("Unable to verify the user");
            }
            log.info("User validated successfully");

            log.info("Generating token");
            String jwtToken = this.jwtService.generateToken(userDetails);
            log.info("Token generated successfully");

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("access_token", jwtToken);
            responseMap.put("validity", "30 min");

            return Result.success(responseMap);
        } catch (Exception e) {
            log.info("Unable to verify the user");
            log.info(e.getMessage());
            return Result.error(e.getMessage());
        }

    }

    public Result<User> insertUser(UserDTO userDTO) {
        try {
            log.info("Got request to insert userDTO with username: {}", userDTO.getUserName());

            if (userDTO.getCampusId() == null || userDTO.getRoleIds() == null || userDTO.getRoleIds().isEmpty()) {
                log.error("Campus ID or Role IDs are missing in the request");
                return Result.error("Campus ID or Role IDs are missing in the request");
            }

            log.info("Getting user details from userDTO");
            User user = getUserDetails(userDTO);

            if (user == null) {
                log.error("Invalid user details provided");
                return Result.error("Invalid user details provided");
            }

            log.info("User details obtained successfully");
            User savedUser = userRepository.save(user);

            log.info("User saved successfully with ID: {}", savedUser.getUserId());

            return Result.success(user);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            log.info("Error while inserting user: {}", e.getMessage());
            String errorMsg;
            if (e instanceof ConstraintViolationException) {
                errorMsg = ((ConstraintViolationException) e).getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));
            } else {
                String message = e.getMessage();
                if (message != null && message.contains("Duplicate entry") && message.contains("user_name")) {
                    errorMsg = "Username already exists.";
                } else if (message != null && message.contains("Duplicate entry") && message.contains("email")) {
                    errorMsg = "Email already exists.";
                } else {
                    errorMsg = "Database integrity error: " + message;
                }
            }
            return Result.error(errorMsg, HttpStatus.BAD_REQUEST);
        } catch (InternalProcessingException e) {
            e.printStackTrace();
            return Result.error(e.getMessage(), e.getResponseStatus());
        }
    }

    public Result<List<User>> getAllUser(String sortBy, boolean ascending) {
        try {
            List<User> userList = new ArrayList<User>();
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            userList = userRepository.findAll(sort);
            return Result.success(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    public Result<User> getUserbyId(long userId) {
        try {
            Optional<User> userOptional = this.userRepository.findById(userId);
            return userOptional.map(Result::success).orElseGet(() -> Result.success(null));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    public Result<User> updateUser(User user) {
        try {
            long id = user.getUserId();

            if (!this.userRepository.existsById(id)) {
                return Result.error("The user with given id " + id + " does not exist.");
            }
            User existingUser = this.userRepository.findById(id).get();
            user = this.getUserDetailsToUpdate(user,existingUser);

            User updatedUser = this.userRepository.save(user);
            return Result.success(updatedUser);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    protected  <T extends User>  T getUserDetailsToUpdate(T user, T userDetails){
        Optional.ofNullable(user.getFirstName())
                .ifPresent(userDetails::setFirstName);
        Optional.ofNullable(user.getLastName())
                .ifPresent(userDetails::setLastName);
        Optional.ofNullable(user.getUserName())
                .ifPresent(userDetails::setUserName);
        Optional.ofNullable(user.getEmail())
                .ifPresent(userDetails::setEmail);
        Optional.ofNullable(user.getPhoneNumber())
                .ifPresent(userDetails::setPhoneNumber);
        Optional.ofNullable(user.getProfilePicture())
                .ifPresent(userDetails::setProfilePicture);
        Optional.ofNullable(user.getRoles())
                .ifPresent(userDetails::setRoles);
        Optional.ofNullable(user.getCampus())
                .ifPresent(userDetails::setCampus);

        return userDetails;
    }

    public Result<User> deleteUserById(long id) {
        try {
            if (!userRepository.existsById(id)) {
                throw new Exception("User with " + id + " does not exists");
            }

            userRepository.deleteById(id);
            return Result.success(null);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<User> deleteUsers(List<Long> ids) {
        try {
            this.userRepository.deleteAllById(ids);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    private User getUserDetails( @NotNull UserDTO userDTO) {
        Set<Role> roleAssigned = new HashSet<>();
        // Ids of role will be passed in payload
        List<Integer> rolesIDs = userDTO.getRoleIds();
        log.info("Roles assigned IDs are :  {}", rolesIDs.toString());
        int campusID = userDTO.getCampusId();

        for (Integer roleID : rolesIDs) {
            if (!roleRepository.existsById(roleID)) {
                log.error("Role with id {} does not exist", roleID);
                 throw  new InternalProcessingException("Role with id " + roleID + " does not exist", HttpStatus.BAD_REQUEST);
            }
            Optional<Role> userRole = roleRepository.findById(roleID);
            roleAssigned.add(userRole.orElse(null));
        }

        if (!campusRepository.existsById(campusID)) {
            log.error("Campus with id {}does not exist", campusID);
            throw new InternalProcessingException("Campus with id " + campusID + " does not exist", HttpStatus.BAD_REQUEST);
        }

        User user = buildUserFromDTO(userDTO);
        user.setRoles(roleAssigned);
        Campus campus = campusRepository.findById(campusID);
        user.setCampus(campus);

        return user;
    }

    protected User buildUserFromDTO(UserDTO userDTO) {
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .userName(userDTO.getUserName())
                .password(encoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .profilePicture(userDTO.getProfilePictureURL())
                .build();

        return user;
    }

    protected Result<User> getUserOtherDetail(@NotNull User user) {

        Set<Role> roleAssigned = new HashSet<>();
        // Ids of role will be passed in payload
        Set<Role> rolesIDs = user.getRoles();
        log.info("Roles assigned IDs are :  " + rolesIDs.toString());
        int campusID = user.getCampus().getId();

        for (Role role : rolesIDs) {
            int roleID = role.getId();
            if (!roleRepository.existsById(roleID)) {
                log.debug("Role with id " + roleID + " does not exist");
                return Result.error("Role with id " + roleID + " does not exist");
            }
            Role userRole = roleRepository.findById(roleID);
            roleAssigned.add(userRole);
        }

        if (!campusRepository.existsById(campusID)) {
            log.debug("Campus with id " + campusID + "does not exist");
            return Result.error("Campus with id " + campusID + " does not exist");
        }

        user.setRoles(roleAssigned);
        Campus campus = campusRepository.findById(campusID);
        user.setCampus(campus);

        return Result.success(user);
    }
}
