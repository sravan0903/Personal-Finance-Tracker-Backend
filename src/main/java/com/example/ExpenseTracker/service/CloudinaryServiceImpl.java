package com.example.ExpenseTracker.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ExpenseTracker.DTO.ImageUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Profile("!test") // 🔥 disables this service during tests
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements ImageStorageService {

    private final Cloudinary cloudinary;

    @Override
    public ImageUploadResult uploadFile(MultipartFile file) {
        validateImage(file);

        try {
            String publicId = "profile/" + UUID.randomUUID();

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "resource_type", "image"
                    )
            );

            return new ImageUploadResult(
                    uploadResult.get("secure_url").toString(),
                    publicId
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public ImageUploadResult updateProfileImage(
            MultipartFile file,
            String oldPublicId
    ) {
        validateImage(file);

        try {
            if (oldPublicId != null && !oldPublicId.isBlank()) {
                cloudinary.uploader().destroy(
                        oldPublicId,
                        ObjectUtils.emptyMap()
                );
            }

            return uploadFile(file);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update profile image", e);
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getContentType() == null ||
            !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("Image size must be <= 2MB");
        }
    }
}
