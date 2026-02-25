package com.example.ExpenseTracker.DTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class IncomeResponseDTO {

    private Long id;
    private String source;
    private Double amount;
    private LocalDate date;
    private String icon;
}
