package service.budget.tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.budget.tracking.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
