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
public class AccountRequest {
    private String accountName;
    private String accountDescription;

    @NotNull
    private AccountType accountType;

    @NotNull
    private long totalAmount;
}
