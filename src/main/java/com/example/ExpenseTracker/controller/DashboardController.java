package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.repository.ExpenseRepository;
import com.example.ExpenseTracker.repository.IncomeRepository;
import com.example.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.ExpenseTracker.entity.Expense;
import com.example.ExpenseTracker.entity.Income;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @GetMapping
    public Map<String, Object> getDashboardData(Authentication authentication) {

        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow()
                .getId();

        // ---------------- TOTALS ----------------
        Double totalIncome = Optional
                .ofNullable(incomeRepository.sumIncomeByUser(userId))
                .orElse(0.0);

        Double totalExpense = Optional
                .ofNullable(expenseRepository.sumExpenseByUser(userId))
                .orElse(0.0);

        Map<String, Object> response = new HashMap<>();
        response.put("totalIncome", totalIncome);
        response.put("totalExpense", totalExpense);
        response.put("totalBalance", totalIncome - totalExpense);

        // ---------------- RECENT TRANSACTIONS ----------------
        List<Map<String, Object>> recentTransactions = new ArrayList<>();

        List<Income> recentIncome =
                incomeRepository.findTop5ByUserIdOrderByDateDesc(userId);

        for (Income i : recentIncome) {
            Map<String, Object> tx = new HashMap<>();
            tx.put("id", i.getId());
            tx.put("type", "income");
            tx.put("source", i.getSource());
            tx.put("amount", i.getAmount());
            tx.put("date", i.getDate());
            tx.put("icon", i.getIcon());
            recentTransactions.add(tx);
        }

        List<Expense> recentExpense =
                expenseRepository.findTop5ByUserIdOrderByDateDesc(userId);

        for (Expense e : recentExpense) {
            Map<String, Object> tx = new HashMap<>();
            tx.put("id", e.getId());
            tx.put("type", "expense");
            tx.put("category", e.getCategory());
            tx.put("amount", e.getAmount());
            tx.put("date", e.getDate());
            tx.put("icon", e.getIcon());
            recentTransactions.add(tx);
        }

        recentTransactions.sort(
                (a, b) -> ((LocalDate) b.get("date"))
                        .compareTo((LocalDate) a.get("date"))
        );

        response.put("recentTransactions", recentTransactions);

        // ---------------- LAST 30 DAYS EXPENSES (BAR CHART) ----------------
        LocalDate last30Days = LocalDate.now().minusDays(30);

        List<Map<String, Object>> last30Expenses =
                expenseRepository.findByUserIdAndDateAfter(userId, last30Days)
                        .stream()
                        .map(e -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", e.getId());
                            map.put("category", e.getCategory());
                            map.put("amount", e.getAmount());
                            map.put("date", e.getDate());
                            map.put("icon", e.getIcon());
                            return map;
                        })
                        .toList();

        response.put(
                "last30DaysExpenses",
                Map.of("transactions", last30Expenses)
        );

        // ---------------- LAST 60 DAYS INCOME (PIE CHART) ----------------
        LocalDate last60Days = LocalDate.now().minusDays(60);

        List<Map<String, Object>> last60Income =
                incomeRepository.findByUserIdAndDateAfter(userId, last60Days)
                        .stream()
                        .map(i -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", i.getId());
                            map.put("source", i.getSource());
                            map.put("amount", i.getAmount());
                            map.put("date", i.getDate());
                            map.put("icon", i.getIcon());

                            return map;
                        })
                        .toList();

        response.put(
                "last60Days",
                Map.of("transactions", last60Income)
        );

        return response;
    }
}
