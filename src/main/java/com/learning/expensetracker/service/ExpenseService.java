package com.learning.expensetracker.service;

import com.learning.expensetracker.dto.ExpenseRequestDTO;
import com.learning.expensetracker.dto.ExpenseResponseDTO;
import com.learning.expensetracker.exception.ExpenseNotFoundException;
import com.learning.expensetracker.model.Expense;
import com.learning.expensetracker.model.enums.ExpenseCategory;
import com.learning.expensetracker.model.enums.PaymentMethod;
import com.learning.expensetracker.repository.ExpenseRepository;
import com.learning.expensetracker.repository.ExpenseSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    public List<ExpenseResponseDTO> findByCategory(ExpenseCategory category) {
        List<Expense> expenses = repo.findByCategory(category);
        List<ExpenseResponseDTO> responseDTOs=new ArrayList<>();
        for (Expense expense: expenses){
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

    public List<ExpenseResponseDTO> findByPaymentMethod(PaymentMethod paymentMethod) {
        List<Expense> expenses = repo.findByPaymentMethod(paymentMethod);
        List<ExpenseResponseDTO> responseDTOs=new ArrayList<>();
        for (Expense expense: expenses){
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

    public List<ExpenseResponseDTO> findByAmountLessThanEqual(BigDecimal maxAmount) {
        List<Expense> expenses = repo.findByAmountLessThanEqual(maxAmount);
        List<ExpenseResponseDTO> responseDTOs=new ArrayList<>();
        for (Expense expense: expenses){
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

    public List<ExpenseResponseDTO> findByDate(LocalDate date) {
        List<Expense> expenses = repo.findByDate(date);
        List<ExpenseResponseDTO> responseDTOs=new ArrayList<>();
        for (Expense expense: expenses){
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

    public List<ExpenseResponseDTO> filterExpenses(
            ExpenseCategory category,
            PaymentMethod paymentMethod,
            BigDecimal maxAmount,
            LocalDate date
    ){
        Specification<Expense> specification =
                Specification.unrestricted();;
        if (category != null) {
            specification = specification.and(
                    ExpenseSpecification.byCategory(category)
            );
        }
        if (paymentMethod != null) {
            specification = specification.and(
                    ExpenseSpecification.byPaymentMethod(paymentMethod)
            );
        }
        if (maxAmount != null) {
            specification = specification.and(
                    ExpenseSpecification.byAmount(maxAmount)
            );
        }
        if (date != null) {
            specification=specification.and(
                    ExpenseSpecification.byDate(date)
            );
        }
        List<Expense> expenses = repo.findAll(specification);
        List<ExpenseResponseDTO> responseDTOs=new ArrayList<>();
        for (Expense expense: expenses){
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

    public BigDecimal getTotalAmount()
    {
        return repo.getTotalAmount();
    }

    public HashMap<ExpenseCategory, BigDecimal> getTotalAmountByCategory()
    {
        List<Object[]> results = repo.getTotalAmountByCategory();
        HashMap<ExpenseCategory, BigDecimal> response=new HashMap<>();
        for (Object[] row : results) {
            ExpenseCategory category = (ExpenseCategory) row[0];
            BigDecimal total = (BigDecimal) row[1];

            response.put(category, total);
        }
        return response;
    }

    public BigDecimal getTotalAmountByDate(LocalDate startDate, LocalDate endDate)
    {
        return repo.getTotalAmountByDate(startDate, endDate);
    }

    public HashMap<PaymentMethod, BigDecimal> getTotalAmountByPaymentMethod() {
        List<Object[]> results = repo.getTotalAmountByPaymentMethod();
        HashMap<PaymentMethod, BigDecimal> response=new HashMap<>();
        for (Object[] row : results) {
            PaymentMethod paymentMethod = (PaymentMethod) row[0];
            BigDecimal total = (BigDecimal) row[1];

            response.put(paymentMethod, total);
        }
        return response;
    }
}
