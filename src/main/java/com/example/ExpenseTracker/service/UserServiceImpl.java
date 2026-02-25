package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.DTO.ImageUploadResult;
import com.example.ExpenseTracker.DTO.UserRequestDTO;
import com.example.ExpenseTracker.DTO.UserResponseDTO;
import com.example.ExpenseTracker.entity.User;
import com.example.ExpenseTracker.exception.DuplicateResourceException;
import com.example.ExpenseTracker.exception.ResourceNotFoundException;
import com.example.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;

    @Override
    public UserResponseDTO createUser(
            UserRequestDTO request,
            MultipartFile profileImage
    ) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword()) // encrypt later
                .build();

        // Upload profile image (optional)
        if (profileImage != null && !profileImage.isEmpty()) {
            ImageUploadResult result =
                    imageStorageService.uploadFile(profileImage);

            user.setProfileImageUrl(result.getImageUrl());
            user.setProfileImagePublicId(result.getPublicId());
        }

        userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    public UserResponseDTO updateUser(
            Long id,
            UserRequestDTO request,
            MultipartFile profileImage
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        user.setFullName(request.getFullName());

        // Update profile image if new one is provided
        if (profileImage != null && !profileImage.isEmpty()) {

            ImageUploadResult result =
                    imageStorageService.updateProfileImage(
                            profileImage,
                            user.getProfileImagePublicId()
                    );

            user.setProfileImageUrl(result.getImageUrl());
            user.setProfileImagePublicId(result.getPublicId());
        }

        userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));
        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        // Delete profile image from Cloudinary
        if (user.getProfileImagePublicId() != null) {
            imageStorageService.updateProfileImage(
                    null,
                    user.getProfileImagePublicId()
            );
        }

        userRepository.delete(user);
    }

    private UserResponseDTO mapToResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
