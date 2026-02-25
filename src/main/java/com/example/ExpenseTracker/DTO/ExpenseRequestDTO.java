package com.example.ExpenseTracker.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequestDTO {

    private String category;
    private Double amount;
    private LocalDate date;
    private String icon;
    private Long userId;
}
