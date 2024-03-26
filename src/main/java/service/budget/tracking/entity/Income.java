package service.budget.tracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long incomeId;

    private long incomeAmount;
    private String incomeCategory;
    private String incomeDescription;
    private Instant incomeDate;
    private String incomePeriod;
}
