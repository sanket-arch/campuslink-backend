package com.api.campuslink.controllers;

import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.FileUploadDTO;
import com.api.campuslink.services.FileHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileHandlerController {
    @Autowired
    private FileHandlerService fileHandlerService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("image") MultipartFile file) {
        Result<FileUploadDTO> result = this.fileHandlerService.upload(file);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
