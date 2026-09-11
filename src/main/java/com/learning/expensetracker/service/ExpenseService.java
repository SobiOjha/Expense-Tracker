package com.learning.expensetracker.service;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.dto.ExpenseResponseDTO;
import com.learning.expensetracker.exception.ExpenseNotFoundException;
import com.learning.expensetracker.model.Expense;
import com.learning.expensetracker.model.enums.ExpenseCategory;
import com.learning.expensetracker.repository.ExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public ExpenseResponseDTO addExpense(ExpenseRequestDTO expense){
        Expense expenseEntity = new Expense();
        expenseEntity.setAmount(expense.getAmount());
        expenseEntity.setDescription(expense.getDescription());
        expenseEntity.setDate(expense.getDate());
        expenseEntity.setCategory(expense.getCategory());
        expenseEntity.setPaymentMethod(expense.getPaymentMethod());
        Expense savedExpense =repo.save(expenseEntity);
        ExpenseResponseDTO responseDTO=new ExpenseResponseDTO();
        responseDTO.setId(savedExpense.getId());
        responseDTO.setAmount(savedExpense.getAmount());
        responseDTO.setDescription(savedExpense.getDescription());
        responseDTO.setDate(savedExpense.getDate());
        responseDTO.setCategory(savedExpense.getCategory());
        responseDTO.setPaymentMethod(savedExpense.getPaymentMethod());
        return responseDTO;
    }

    public List<ExpenseResponseDTO> getAllExpenses(){
        List<Expense> expenses=repo.findAll();
        List<ExpenseResponseDTO> responseDTOs=new ArrayList<>();
        for (Expense expense:expenses){
            ExpenseResponseDTO responseDTO=new ExpenseResponseDTO();
            responseDTO.setId(expense.getId());
            responseDTO.setAmount(expense.getAmount());
            responseDTO.setDescription(expense.getDescription());
            responseDTO.setDate(expense.getDate());
            responseDTO.setCategory(expense.getCategory());
            responseDTO.setPaymentMethod(expense.getPaymentMethod());
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    public ExpenseResponseDTO getExpenseById(Integer id)  {
        Expense expense= repo.findById(id).orElseThrow(()->new ExpenseNotFoundException("Expense not found of ID:"+ id));
        ExpenseResponseDTO responseDTO=new ExpenseResponseDTO();
        responseDTO.setId(expense.getId());
        responseDTO.setAmount(expense.getAmount());
        responseDTO.setDescription(expense.getDescription());
        responseDTO.setDate(expense.getDate());
        responseDTO.setCategory(expense.getCategory());
        responseDTO.setPaymentMethod(expense.getPaymentMethod());
        return responseDTO;
    }

    public ExpenseResponseDTO updateExpenseById(Integer id, ExpenseRequestDTO expense) {
        Expense expense1= repo.findById(id).orElseThrow(()->new ExpenseNotFoundException("Expense not found of ID:"+ id));
        expense1.setAmount(expense.getAmount());
        expense1.setDescription(expense.getDescription());
        expense1.setDate(expense.getDate());
        expense1.setCategory(expense.getCategory());
        expense1.setPaymentMethod(expense.getPaymentMethod());
        Expense savedExpense =repo.save(expense1);
        ExpenseResponseDTO responseDTO=new ExpenseResponseDTO();
        responseDTO.setId(savedExpense.getId());
        responseDTO.setAmount(savedExpense.getAmount());
        responseDTO.setDescription(savedExpense.getDescription());
        responseDTO.setDate(savedExpense.getDate());
        responseDTO.setCategory(savedExpense.getCategory());
        responseDTO.setPaymentMethod(savedExpense.getPaymentMethod());
        return responseDTO;
    }

    public void deleteExpenseById(Integer id) {
        Expense expense1 = repo.findById(id).orElseThrow(()->new ExpenseNotFoundException("Expense not found of ID:"+ id));
        repo.delete(expense1);
    }
}
