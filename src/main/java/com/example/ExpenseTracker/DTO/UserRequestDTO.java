

package com.example.ExpenseTracker.DTO;

import lombok.Data;
@Data
public class UserRequestDTO {

    private String fullName;
    private String email;
    private String password;
    private String profileImageUrl;
    private String profileImagePublicId;
}