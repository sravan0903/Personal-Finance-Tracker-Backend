package com.example.ExpenseTracker.repository;

import com.example.ExpenseTracker.entity.Income;
import com.example.ExpenseTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId")
    Double sumIncomeByUser(Long userId);
    
    int deleteByIdAndUserEmail(Long id, String email);
    
    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId")
    Double totalIncome(@Param("userId") Long userId);

    List<Income> findTop5ByUserIdOrderByDateDesc(Long userId);

    List<Income> findByUserIdAndDateAfter(Long userId, LocalDate date);
}
