package service.budget.tracking.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import service.budget.tracking.model.ExpenseCategory;

import java.time.Instant;

@Entity
@Table(name = "expense", schema = "tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long expenseId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @NotNull
    private double expenseAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    private String expenseDescription;
    private Instant expenseDate;
    private String expensePeriod;
}
