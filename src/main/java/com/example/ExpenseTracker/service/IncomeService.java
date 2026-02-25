package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.DTO.IncomeRequestDTO;
import com.example.ExpenseTracker.DTO.IncomeResponseDTO;

import java.util.List;

public interface IncomeService {

    IncomeResponseDTO createIncome(IncomeRequestDTO request, String email);

    List<IncomeResponseDTO> getIncomesForLoggedInUser(String email);

    IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO request, String email);

    void deleteIncome(Long id, String email);
}
