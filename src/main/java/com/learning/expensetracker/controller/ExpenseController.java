package com.learning.expensetracker.controller;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.model.Expense;
import com.learning.expensetracker.service.ExpenseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
