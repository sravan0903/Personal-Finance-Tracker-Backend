package com.example.ExpenseTracker.controller;

import com.example.ExpenseTracker.DTO.ExpenseRequestDTO;
import com.example.ExpenseTracker.DTO.ExpenseResponseDTO;
import com.example.ExpenseTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @RequestBody ExpenseRequestDTO request,
            Authentication authentication) {

        return new ResponseEntity<>(
                expenseService.createExpense(request, authentication.getName()),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            Authentication authentication) {

        return ResponseEntity.ok(
                expenseService.getExpensesForLoggedInUser(
                        authentication.getName()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequestDTO request,
            Authentication authentication) {

        return ResponseEntity.ok(
                expenseService.updateExpense(
                        id, request, authentication.getName()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id,
            Authentication authentication) {

        expenseService.deleteExpense(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
