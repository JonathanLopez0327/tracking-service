package service.budget.tracking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import service.budget.tracking.model.IncomeCategory;

import java.time.Instant;

@Entity
@Table(name = "income", schema = "tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long incomeId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @NotNull
    private double incomeAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;

    private String incomeDescription;
    private Instant incomeDate;
    private String incomePeriod;
}
