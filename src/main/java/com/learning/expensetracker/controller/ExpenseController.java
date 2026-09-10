package com.learning.expensetracker.controller;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.model.Expense;
import com.learning.expensetracker.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping("/expenses")
    public Expense addExpense(@RequestBody ExpenseRequestDTO expense){
        return service.addExpense(expense);
    }

    @GetMapping("/expenses")
    public List<Expense> getAllExpenses(){
        return service.getAllExpenses();
    }

    @GetMapping("/expenses/{id}")
    public Expense getExpenseById(@PathVariable Integer id){
        return service.getExpenseById(id);
    }
}
