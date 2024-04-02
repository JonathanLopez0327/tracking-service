package service.budget.tracking.model;


import lombok.Getter;

@Getter
public enum IncomeCategory {
    LINEAR_INCOME("LINEAR_INCOME"),
    PASSIVE_INCOME("PASSIVE_INCOME"),
    DIFFERENCE("DIFFERENCE"),
    OTHERS("OTHERS");

    private String value;
    IncomeCategory(String value) {
        this.value = value;
    }
}
