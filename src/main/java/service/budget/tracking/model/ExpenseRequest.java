package service.budget.tracking.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {
    @NotNull
    @Min(1)
    private long expenseAmount;

    @NotEmpty
    private String expenseCategory;

    @NotEmpty
    private String expenseDescription;
}
