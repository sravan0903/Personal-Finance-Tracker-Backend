package com.example.ExpenseTracker.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ExpenseResponseDTO {

    private Long id;
    private String category;
    private Double amount;
    private LocalDate date;
    private String icon;
}
