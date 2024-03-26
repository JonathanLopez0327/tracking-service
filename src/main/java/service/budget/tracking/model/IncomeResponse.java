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
public class IncomeResponse {
    private long incomeId;

    private long incomeAmount;
    private String incomeCategory;
    private String incomeDescription;
    private Instant incomeDate;
    private String incomePeriod;
}
