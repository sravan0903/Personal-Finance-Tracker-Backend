package com.example.ExpenseTracker.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;   // used as username for JWT

    @Column(nullable = false)
    private String password; // BCrypt hash

    // ✅ Cloudinary image URL (used by frontend)
    @Column(length = 500)
    private String profileImageUrl;

    // ✅ Cloudinary public_id (used for delete/update)
    @Column(length = 255)
    private String profileImagePublicId;
}

