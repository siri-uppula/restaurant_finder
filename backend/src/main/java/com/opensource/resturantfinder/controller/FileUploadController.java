package com.opensource.resturantfinder.controller;

import com.opensource.resturantfinder.common.ApiResponse;
import com.opensource.resturantfinder.common.ErrorDetails;
import com.opensource.resturantfinder.service.impl.AWSS3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Upload", description = "File Upload API")
public class FileUploadController {
    @Autowired
    private AWSS3UploadService awsS3UploadService;

    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Upload a file and get url")
    public ResponseEntity<ApiResponse<String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-Request-ID") String requestId) {
        try {
            String fileUrl = awsS3UploadService.uploadFile(file);
            return ResponseEntity.ok(ApiResponse.success(fileUrl, requestId));
        } catch (IOException e) {
            ErrorDetails errorDetails = new ErrorDetails("FILE_UPLOAD_ERROR", "Failed to upload file", null);
            return ResponseEntity.ok(ApiResponse.error(errorDetails, requestId));
        }
    }
}