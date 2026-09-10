package com.learning.expensetracker.service;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.exception.ExpenseNotFoundException;
import com.learning.expensetracker.model.Expense;
import com.learning.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public Expense addExpense(ExpenseRequestDTO expense){
        Expense expenseEntity = new Expense();
        expenseEntity.setAmount(expense.getAmount());
        expenseEntity.setDescription(expense.getDescription());
        expenseEntity.setDate(expense.getDate());
        expenseEntity.setCategory(expense.getCategory());
        expenseEntity.setPaymentMethod(expense.getPaymentMethod());
        return repo.save(expenseEntity);
    }

    public List<Expense> getAllExpenses(){
        return repo.findAll();
    }

    public Expense getExpenseById(Integer id)  {
        return repo.findById(id).orElseThrow(()->new ExpenseNotFoundException("Expense not found"));
    }
}
