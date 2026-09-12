package com.learning.expensetracker.repository;

import com.learning.expensetracker.model.Expense;
import com.learning.expensetracker.model.enums.ExpenseCategory;
import com.learning.expensetracker.model.enums.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSpecification {
    public static Specification<Expense> byCategory(ExpenseCategory category) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);

    }

    public static Specification<Expense> byPaymentMethod(PaymentMethod paymentMethod) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod);

    }

    public static Specification<Expense> byDate(LocalDate expenseDate) {
        return (root, query, criteriaBuilder) ->  criteriaBuilder.equal(root.get("date"), expenseDate);
    }

    public static Specification<Expense> byAmount(BigDecimal expenseAmount) {
        return (root, query, criteriaBuilder) ->  criteriaBuilder.lessThanOrEqualTo(root.get("amount"), expenseAmount);
    }
}
