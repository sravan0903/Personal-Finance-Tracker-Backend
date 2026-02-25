package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.DTO.ExpenseRequestDTO;
import com.example.ExpenseTracker.DTO.ExpenseResponseDTO;

import java.util.List;

public interface ExpenseService {

    ExpenseResponseDTO createExpense(ExpenseRequestDTO request, String email);

    List<ExpenseResponseDTO> getExpensesForLoggedInUser(String email);

    ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO request, String email);

    void deleteExpense(Long id, String email);
}
