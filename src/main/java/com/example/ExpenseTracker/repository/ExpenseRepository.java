package com.example.ExpenseTracker.repository;

import com.example.ExpenseTracker.entity.Expense;
import com.example.ExpenseTracker.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByUser(User user);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    Double sumExpenseByUser(Long userId);
    
    void deleteByIdAndUserEmail(Long id, String email);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    Double totalExpense(@Param("userId") Long userId);

    List<Expense> findTop5ByUserIdOrderByDateDesc(Long userId);

    List<Expense> findByUserIdAndDateAfter(Long userId, LocalDate date);
}
