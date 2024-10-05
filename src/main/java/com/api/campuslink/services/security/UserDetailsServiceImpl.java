package com.api.campuslink.services.security;

import com.api.campuslink.dao.UserRespository;
import com.api.campuslink.models.entities.User;
import com.api.campuslink.models.entities.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRespository userRespository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching user details from DB for authentication");
        User user = userRespository.findByUserName(username);

        if(user== null){
            log.error("User not found");
            throw new UsernameNotFoundException("User Not Found");
        }
        log.info("Fetched user successfully.");
        return new UserPrincipal(user);
    }
}
