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
    private long expenseAmount;
    private String expenseCategory;
    private String expenseDescription;
    private Instant expenseDate;
    private String period;
}
