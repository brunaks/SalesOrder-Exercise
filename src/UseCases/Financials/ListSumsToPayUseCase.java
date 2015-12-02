package UseCases.Financials;

import Entities.FinancialRecords.SumToPayInfo;
import Interfaces.Persistence.SumToPayRepository;

import java.util.List;

/**
 * Created by Bruna Koch Schmitt on 02/12/2015.
 */
public class ListSumsToPayUseCase {

    private SumToPayRepository repository;

    public ListSumsToPayUseCase(SumToPayRepository repository) {
        this.repository = repository;
    }

    public List<SumToPayInfo> getAll() {
        return this.repository.getAll();
    }
}
