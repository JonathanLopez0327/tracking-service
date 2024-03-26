package service.budget.tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.budget.tracking.entity.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
}
