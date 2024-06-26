package service.budget.tracking.service;

import service.budget.tracking.model.IncomeRequest;
import service.budget.tracking.model.IncomeResponse;

import java.util.List;

public interface IncomeService {
    long createIncome(IncomeRequest request);

    List<IncomeResponse> getAllIncome();

    IncomeResponse getIncomeById(long id);

    void deleteIncomeById(long id);

    void updateIncomeById(long id, IncomeRequest request);

    List<IncomeResponse> getIncomeByAccount(long id);
}
