package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.DTO.IncomeRequestDTO;
import com.example.ExpenseTracker.DTO.IncomeResponseDTO;
import com.example.ExpenseTracker.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> createIncome(
            @RequestBody IncomeRequestDTO request,
            Authentication authentication) {

        return new ResponseEntity<>(
                incomeService.createIncome(
                        request,
                        authentication.getName()
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> getIncomes(
            Authentication authentication) {

        return ResponseEntity.ok(
                incomeService.getIncomesForLoggedInUser(
                        authentication.getName()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> updateIncome(
            @PathVariable Long id,
            @RequestBody IncomeRequestDTO request,
            Authentication authentication) {

        return ResponseEntity.ok(
                incomeService.updateIncome(
                        id, request, authentication.getName()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(
            @PathVariable Long id,
            Authentication authentication) {

        incomeService.deleteIncome(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
