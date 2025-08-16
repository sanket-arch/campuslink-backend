package com.api.campuslink.services;

import com.api.campuslink.dao.UserRepository;
import com.api.campuslink.exceptions.InternalProcessingException;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.entities.User;
import com.api.campuslink.services.security.JwtService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisService redisService;

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

            if (userDetails == null) {
                log.error("User with username {} does not exist", username);
                return Result.error("User with username " + username + " does not exist");
            }

            log.info("User with username {} exists, proceeding to authentication", username);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (!authentication.isAuthenticated()) {
                log.debug("Unable to verify the user");
                return Result.error("Unable to verify the user");
            }
            log.info("User validated successfully");

            Map<String, String> responseMap = createTokenResponse(userDetails);
            return Result.success(responseMap);
        } catch (Exception e) {
            log.info("Unable to verify the user");
            log.info(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<Object> refreshToken(String refreshToken) {
        try {
            log.info("Got request to refresh token");
            if (refreshToken == null || refreshToken.isEmpty()) {
                log.error("Refresh token is missing");
                return Result.error("Refresh token is missing");
            }

            String username = jwtService.getUsername(refreshToken);
            if (username == null) {
                log.error("Invalid refresh token");
                return Result.error("Invalid refresh token");
            }

            User userDetails = userRepository.findByUserName(username);
            if (userDetails == null) {
                log.error("User with username {} does not exist", username);
                return Result.error("User with username " + username + " does not exist");
            }

            log.info("Verifying refresh token for user: {}", username);
            verifyRefreshToken(userDetails, refreshToken);

            Map<String, String> responseMap = createTokenResponse(userDetails);

            return Result.success(responseMap);
        } catch (InternalProcessingException e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return Result.error(e.getMessage(), e.getResponseStatus());
        }
    }

    private Map<String, String> createTokenResponse(User userDetails) {
        String username = userDetails.getUserName();
        log.info("Generating token");
        String jwtToken = this.jwtService.generateToken(userDetails);
        log.info("Token generated successfully");

        log.info("Generating refresh token");
        String refreshToken = this.jwtService.generateRefreshToken(userDetails);
        log.info("Refresh token generated successfully");

        log.info("Storing token in Redis for user: {}", username);
        redisService.setValue(username, refreshToken, 7 * 24 * 60 * 60 * 1000L); // Store for 7 days
        log.info("Token stored in Redis successfully");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("access_token", jwtToken);
        responseMap.put("refresh_token", refreshToken);
        responseMap.put("validity", "30 min");

        return responseMap;
    }

    private void verifyRefreshToken(User user, String token) {
        Object refreshToken = redisService.getValue(user.getUserName());
        if (refreshToken == null || !refreshToken.equals(token)) {
            log.debug("Invalid refresh token for user: {}", user.getUserName());
            throw new InternalProcessingException("Invalid refresh token", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (jwtService.isTokenExpired(token)) {
            log.debug("Refresh token is expired for user: {}", user.getUserName());
            throw new InternalProcessingException("Refresh token is expired", HttpStatus.UNAUTHORIZED);
        }
    }
}
