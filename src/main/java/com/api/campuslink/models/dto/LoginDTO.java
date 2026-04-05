package com.api.campuslink.models.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
