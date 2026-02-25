package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.DTO.ImageUploadResult;
import com.example.ExpenseTracker.entity.User;
import com.example.ExpenseTracker.repository.UserRepository;
import com.example.ExpenseTracker.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final ImageStorageService imageStorageService;
    private final UserRepository userRepository;

    // 🔹 Generic upload (returns URL only)
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        ImageUploadResult result =
                imageStorageService.uploadFile(file);

        return ResponseEntity.ok(
                Map.of("imageUrl", result.getImageUrl())
        );
    }

    @PostMapping("/update/profile")
    public ResponseEntity<Map<String, String>> updateProfileImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ImageUploadResult result =
                imageStorageService.updateProfileImage(	
                        file,
                        user.getProfileImagePublicId()
                );

        user.setProfileImageUrl(result.getImageUrl());
        user.setProfileImagePublicId(result.getPublicId());

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("imageUrl", result.getImageUrl())
        );
    }
}
