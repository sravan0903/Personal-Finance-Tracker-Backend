package com.example.ExpenseTracker.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Double totalBalance;
    private Double totalIncome;
    private Double totalExpense;

    private List<TransactionDTO> recentTransactions;
    private ExpenseGroup last30DaysExpenses;
    private IncomeGroup last60Days;
}
