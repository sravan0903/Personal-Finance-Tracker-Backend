package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.DTO.UserRequestDTO;
import com.example.ExpenseTracker.DTO.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(
            UserRequestDTO request,
            MultipartFile profileImage
    );

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(
            Long id,
            UserRequestDTO request,
            MultipartFile profileImage
    );

    void deleteUser(Long id);
}
