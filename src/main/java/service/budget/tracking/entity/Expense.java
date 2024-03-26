package service.budget.tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long expenseId;

    private long expenseAmount;
    private String expenseCategory;
    private String expenseDescription;
    private Instant expenseDate;
    private String period;
}
