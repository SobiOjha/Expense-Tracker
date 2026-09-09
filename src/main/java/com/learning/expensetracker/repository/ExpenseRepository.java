package com.learning.expensetracker.repository;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
}
