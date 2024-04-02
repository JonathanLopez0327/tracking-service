package service.budget.tracking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetailsReponse {
    private long accountId;
    private String accountName;
    private String accountDescription;
    private AccountType accountType;
    private double totalAmount;

    List<IncomeResponse> incomeDetails;
    List<ExpenseResponse> expenseDetails;
}
