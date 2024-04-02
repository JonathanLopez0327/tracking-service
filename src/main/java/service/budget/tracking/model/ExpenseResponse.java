package service.budget.tracking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {
    private long expenseId;
    private long accountId;
    private double expenseAmount;
    private ExpenseCategory expenseCategory;
    private String expenseDescription;
    private Instant expenseDate;
    private String expensePeriod;
}
