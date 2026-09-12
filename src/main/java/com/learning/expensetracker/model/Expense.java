package com.learning.expensetracker.model;
import com.learning.expensetracker.dto.ExpenseResponseDTO;
import com.learning.expensetracker.model.enums.ExpenseCategory;
import com.learning.expensetracker.model.enums.PaymentMethod;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
    private ExpenseResponseDTO mapToResponseDTO(Expense expense) {
        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

        responseDTO.setId(expense.getId());
        responseDTO.setAmount(expense.getAmount());
        responseDTO.setDescription(expense.getDescription());
        responseDTO.setDate(expense.getDate());
        responseDTO.setCategory(expense.getCategory());
        responseDTO.setPaymentMethod(expense.getPaymentMethod());

        return responseDTO;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
}
