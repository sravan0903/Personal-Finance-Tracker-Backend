package com.example.ExpenseTracker.service;


import com.example.ExpenseTracker.DTO.IncomeRequestDTO;
import com.example.ExpenseTracker.DTO.IncomeResponseDTO;
import com.example.ExpenseTracker.entity.Income;
import com.example.ExpenseTracker.entity.User;
import com.example.ExpenseTracker.exception.ResourceNotFoundException;
import com.example.ExpenseTracker.repository.IncomeRepository;
import com.example.ExpenseTracker.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Override
    public IncomeResponseDTO createIncome(
            IncomeRequestDTO request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Income income = Income.builder()
                .source(request.getSource())
                .amount(request.getAmount())
                .date(request.getDate())
                .icon(request.getIcon())
                .user(user)
                .build();

        return mapToDto(incomeRepository.save(income));
    }

    @Override
    public List<IncomeResponseDTO> getIncomesForLoggedInUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return incomeRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public IncomeResponseDTO updateIncome(
            Long id, IncomeRequestDTO request, String email) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));

        if (!income.getUser().getEmail().equals(email)) {
            throw new SecurityException("Not allowed");
        }

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setDate(request.getDate());
        income.setIcon(request.getIcon());

        return mapToDto(incomeRepository.save(income));
    }

    @Transactional
    @Override
    public void deleteIncome(Long id, String email) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));

        if (!income.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Not allowed");
        }

        incomeRepository.delete(income);
    }

    private IncomeResponseDTO mapToDto(Income income) {
        return IncomeResponseDTO.builder()
                .id(income.getId())
                .source(income.getSource())
                .amount(income.getAmount())
                .date(income.getDate())
                .icon(income.getIcon())
                .build();
    }
}
