package com.learning.expensetracker.repository;

import com.learning.expensetracker.model.Expense;
//import com.learning.expensetracker.model.enums.ExpenseCategory;
//import com.learning.expensetracker.model.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer>, JpaSpecificationExecutor<Expense> {
//    List<Expense> findByCategory(ExpenseCategory category);
//    List<Expense> findByPaymentMethod(PaymentMethod paymentMethod);
//    List<Expense> findByAmountLessThanEqual(BigDecimal maxAmount);
//    List<Expense> findByDate(LocalDate date);

    @Query("SELECT SUM(e.amount) FROM Expense e")
    BigDecimal getTotalAmount();

    @Query("""
       SELECT e.category, SUM(e.amount)
       FROM Expense e
       GROUP BY e.category
       """)
    List<Object[]> getTotalAmountByCategory();

    @Query("""
       SELECT SUM(e.amount)
       FROM Expense e
       WHERE e.date BETWEEN :startDate AND :endDate
       """)
    BigDecimal getTotalAmountByDate(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    @Query("""
       SELECT e.paymentMethod, SUM(e.amount)
       FROM Expense e
       GROUP BY e.paymentMethod
       """)
    List<Object[]> getTotalAmountByPaymentMethod();
}
