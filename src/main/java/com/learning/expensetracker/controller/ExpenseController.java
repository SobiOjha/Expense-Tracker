package com.learning.expensetracker.controller;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.dto.ExpenseResponseDTO;
import com.learning.expensetracker.model.enums.ExpenseCategory;
import com.learning.expensetracker.model.enums.PaymentMethod;
import jakarta.validation.Valid;
import com.learning.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


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

//    @GetMapping("/expenses/filter/category")
//    public List<ExpenseResponseDTO> findByCategory(@RequestParam ExpenseCategory category){
//        return service.findByCategory(category);
//    }
//
//    @GetMapping("/expenses/filter/payment-method")
//    public List<ExpenseResponseDTO> findByPaymentMethod(@RequestParam PaymentMethod paymentMethod){
//        return service.findByPaymentMethod(paymentMethod);
//    }
//
//    @GetMapping("/expenses/filter/max-amount")
//    public List<ExpenseResponseDTO> findByAmountLessThanEqual(@RequestParam BigDecimal maxAmount){
//        return service.findByAmountLessThanEqual(maxAmount);
//    }
//
//    @GetMapping("/expenses/filter/date")
//    public List<ExpenseResponseDTO> findByDate(@RequestParam LocalDate date){
//        return service.findByDate(date);
//    }

    @GetMapping("/expenses/filter")
    public List<ExpenseResponseDTO> filterExpenses(
            @RequestParam(required = false) ExpenseCategory category,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) LocalDate date) {

        return service.filterExpenses(
                category,
                paymentMethod,
                maxAmount,
                date
        );
    }

    @GetMapping("/expenses/stats/total")
    public BigDecimal getTotalExpenses(){
        return service.getTotalAmount();
    }

    @GetMapping("/expenses/stats/category")
    public Map<ExpenseCategory, BigDecimal> getTotalExpensesByCategory(){
        return service.getTotalAmountByCategory();
    }

    @GetMapping("/expenses/stats/date")
    public BigDecimal getTotalExpensesByDate(@RequestParam LocalDate startDate,
                                             @RequestParam LocalDate endDate){
        return service.getTotalAmountByDate(startDate, endDate);
    }

    @GetMapping("/expenses/stats/payment-method")
    public Map<PaymentMethod, BigDecimal> getTotalExpensesByPaymentMethod(){
        return service.getTotalAmountByPaymentMethod();
    }
}
