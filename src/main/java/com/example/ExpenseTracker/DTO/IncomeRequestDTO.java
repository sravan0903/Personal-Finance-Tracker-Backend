package com.example.ExpenseTracker.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeRequestDTO {

    private String source;
    private Double amount;
    private LocalDate date;
    private String icon;
}
