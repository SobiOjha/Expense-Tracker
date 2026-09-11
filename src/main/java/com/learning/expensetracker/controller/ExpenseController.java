package com.learning.expensetracker.controller;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.dto.ExpenseResponseDTO;
import com.learning.expensetracker.model.enums.ExpenseCategory;
import jakarta.validation.Valid;
import com.learning.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping("/expenses")
    public ExpenseResponseDTO addExpense(@Valid @RequestBody ExpenseRequestDTO expense){
        return service.addExpense(expense);
    }

    @GetMapping("/expenses")
    public List<ExpenseResponseDTO> getAllExpenses(){
        return service.getAllExpenses();
    }

    @GetMapping("/expenses/{id}")
    public ExpenseResponseDTO getExpenseById(@PathVariable Integer id){
        return service.getExpenseById(id);
    }

    @PutMapping("/expenses/{id}")
    public ExpenseResponseDTO updateExpenseById(
            @PathVariable Integer id,
            @Valid @RequestBody ExpenseRequestDTO expense
    )
    {
        return service.updateExpenseById(id,expense);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<String> deleteExpenseById(@PathVariable Integer id){
        service.deleteExpenseById(id);
        return new ResponseEntity<>(
                "Expense deleted successfully of the ID:"+ id,
                HttpStatus.OK
        );
    }


}
