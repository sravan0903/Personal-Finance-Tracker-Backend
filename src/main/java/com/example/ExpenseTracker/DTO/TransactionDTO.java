package com.example.ExpenseTracker.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private String type;     // "income" or "expense"
    private String category;
    private String source;
    private Double amount;
    private LocalDate date;
    private String icon;
}
