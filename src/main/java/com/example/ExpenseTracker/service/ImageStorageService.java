package com.example.ExpenseTracker.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.ExpenseTracker.DTO.ImageUploadResult;

public interface ImageStorageService {

    ImageUploadResult uploadFile(MultipartFile file);

    ImageUploadResult updateProfileImage(
            MultipartFile file,
            String oldPublicId
    );
}
