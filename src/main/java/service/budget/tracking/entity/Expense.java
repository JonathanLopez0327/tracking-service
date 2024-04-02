package service.budget.tracking.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import service.budget.tracking.model.ExpenseCategory;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long expenseId;
    private long accountId;

    private double expenseAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    private String expenseDescription;
    private Instant expenseDate;
    private String expensePeriod;
}
