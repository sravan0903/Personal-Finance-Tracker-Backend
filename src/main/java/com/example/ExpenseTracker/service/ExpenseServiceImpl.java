package com.example.ExpenseTracker.service;

import com.example.ExpenseTracker.DTO.ExpenseRequestDTO;
import com.example.ExpenseTracker.DTO.ExpenseResponseDTO;
import com.example.ExpenseTracker.entity.Expense;
import com.example.ExpenseTracker.entity.User;
import com.example.ExpenseTracker.exception.ResourceNotFoundException;
import com.example.ExpenseTracker.repository.ExpenseRepository;
import com.example.ExpenseTracker.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;


import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public ExpenseResponseDTO createExpense(
            ExpenseRequestDTO request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = Expense.builder()
                .category(request.getCategory())
                .amount(request.getAmount())
                .date(request.getDate())
                .icon(request.getIcon())
                .user(user)
                .build();

        return mapToDto(expenseRepository.save(expense));
    }

    @Override
    public List<ExpenseResponseDTO> getExpensesForLoggedInUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return expenseRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ExpenseResponseDTO updateExpense(
            Long id, ExpenseRequestDTO request, String email) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        // 🔐 ownership check
        if (!expense.getUser().getEmail().equals(email)) {
            throw new SecurityException("Not allowed");
        }

        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setIcon(request.getIcon());

        return mapToDto(expenseRepository.save(expense));
    }

    @Transactional
    @Override
    public void deleteExpense(Long id, String email) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Not allowed");
        }

        expenseRepository.delete(expense);
    }


    private ExpenseResponseDTO mapToDto(Expense expense) {
        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .icon(expense.getIcon())
                .build();
    }
}
