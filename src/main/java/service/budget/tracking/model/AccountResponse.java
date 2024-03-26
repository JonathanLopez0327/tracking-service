package service.budget.tracking.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private long accountId;
    private String accountName;
    private String accountDescription;
    private AccountType accountType;
    private long totalAmount;
}
