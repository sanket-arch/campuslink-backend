package com.api.campuslink.services;

import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.FileUploadDTO;
import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class FileHandlerService {

    @Autowired
    private Cloudinary cloudinary;

    public Result<FileUploadDTO> upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(),Map.of());
            log.info("File uploaded successfully : {}", data.get("url"));

            FileUploadDTO fileUploadDTO = FileUploadDTO.builder()
                    .format((String) data.get("format"))
                    .resourceType((String) data.get("resource_type"))
                    .secureUrl((String) data.get("secure_url"))
                    .createdAt((String) data.get("created_at"))
                    .assetId((String) data.get("asset_id"))
                    .url((String) data.get("url"))
                    .publicId((String) data.get("public_id"))
                    .bytes((Integer) data.get("bytes"))
                    .build();

            return Result.success(fileUploadDTO);
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            return Result.error("Error uploading file: " + e.getMessage());
        }
    }
}
