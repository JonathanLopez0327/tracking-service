package service.budget.tracking.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeRequest {
    @NotNull
    @Min(1)
    private double incomeAmount;
    private long accountId;
    private IncomeCategory incomeCategory;
    private String incomeDescription;
}
