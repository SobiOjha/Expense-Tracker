package com.learning.expensetracker.dto;

import com.learning.expensetracker.model.enums.ExpenseCategory;
import com.learning.expensetracker.model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequestDTO {
    private BigDecimal amount;
    private ExpenseCategory category;
    private String description;
    private LocalDate date;
    private PaymentMethod paymentMethod;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
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
}
