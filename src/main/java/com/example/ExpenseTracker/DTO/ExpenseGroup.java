package com.example.ExpenseTracker.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseGroup {
    private List<TransactionDTO> transactions;
}
