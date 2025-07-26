package com.api.campuslink.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadDTO {
    private String format;
    private String resourceType;
    private String secureUrl;
    private String createdAt;
    private String assetId;
    private String url;
    private String publicId;
    private int bytes;
}
