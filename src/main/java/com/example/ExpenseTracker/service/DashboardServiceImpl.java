package com.example.ExpenseTracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ExpenseTracker.DTO.DashboardResponse;
import com.example.ExpenseTracker.DTO.ExpenseGroup;
import com.example.ExpenseTracker.DTO.IncomeGroup;
import com.example.ExpenseTracker.DTO.TransactionDTO;
import com.example.ExpenseTracker.repository.ExpenseRepository;
import com.example.ExpenseTracker.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl {

    private final ExpenseRepository expenseRepo;
    private final IncomeRepository incomeRepo;

    public DashboardResponse getDashboardData(Long userId) {

        Double totalIncome = Optional.ofNullable(incomeRepo.totalIncome(userId)).orElse(0.0);
        Double totalExpense = Optional.ofNullable(expenseRepo.totalExpense(userId)).orElse(0.0);
        Double totalBalance = totalIncome - totalExpense;

        // Recent Transactions (mix)
        List<TransactionDTO> recentTransactions = new ArrayList<>();

        incomeRepo.findTop5ByUserIdOrderByDateDesc(userId)
                .forEach(i -> recentTransactions.add(
                        new TransactionDTO(
                                i.getId(),
                                "income",
                                null,
                                i.getSource(),
                                i.getAmount(),
                                i.getDate(),
                                null
                        )
                ));

        expenseRepo.findTop5ByUserIdOrderByDateDesc(userId)
                .forEach(e -> recentTransactions.add(
                        new TransactionDTO(
                                e.getId(),
                                "expense",
                                e.getCategory(),
                                null,
                                e.getAmount(),
                                e.getDate(),
                                null
                        )
                ));

        recentTransactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        // Last 30 Days Expenses
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<TransactionDTO> last30Expenses = expenseRepo
                .findByUserIdAndDateAfter(userId, thirtyDaysAgo)
                .stream()
                .map(e -> new TransactionDTO(
                        e.getId(),
                        "expense",
                        e.getCategory(),
                        null,
                        e.getAmount(),
                        e.getDate(),
                        null
                ))
                .toList();

        // Last 60 Days Income
        LocalDate sixtyDaysAgo = LocalDate.now().minusDays(60);
        List<TransactionDTO> last60Income = incomeRepo
                .findByUserIdAndDateAfter(userId, sixtyDaysAgo)
                .stream()
                .map(i -> new TransactionDTO(
                        i.getId(),
                        "income",
                        null,
                        i.getSource(),
                        i.getAmount(),
                        i.getDate(),
                        null
                ))
                .toList();

        return new DashboardResponse(
                totalBalance,
                totalIncome,
                totalExpense,
                recentTransactions,
                new ExpenseGroup(last30Expenses),
                new IncomeGroup(last60Income)
        );
    }
}
