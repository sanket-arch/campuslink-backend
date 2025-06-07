package com.api.campuslink.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private Long phoneNumber;
    private Integer campusId;
    private List<Integer> roleIds;
    private String profilePictureURL;
}
